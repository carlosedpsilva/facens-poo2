package br.facens.poo2.ac2project.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.facens.poo2.ac2project.dto.mapper.EventMapper;
import br.facens.poo2.ac2project.dto.mapper.TicketMapper;
import br.facens.poo2.ac2project.dto.request.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.request.TicketInsertRequest;
import br.facens.poo2.ac2project.dto.response.EventFindResponse;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Attendee;
import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Place;
import br.facens.poo2.ac2project.enums.TicketType;
import br.facens.poo2.ac2project.exception.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.EventNotFoundException;
import br.facens.poo2.ac2project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.IllegalScheduleException;
import br.facens.poo2.ac2project.exception.PlaceNotFoundException;
import br.facens.poo2.ac2project.exception.TicketNotAvailableException;
import br.facens.poo2.ac2project.repository.EventRepository;
import br.facens.poo2.ac2project.repository.TicketRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private static final String SAVED_MESSAGE = "Saved Event with ID ";
  private static final String DELETED_MESSAGE = "Deleted Event with ID ";
  private static final String UPDATED_MESSAGE = "Updated Event with ID ";

  private EventRepository eventRepository;
  private TicketRepository ticketRepository;

  private AdminService adminService;
  private PlaceService placeService;
  private AttendeeService attendeeService;

  private EventMapper eventMapper;
  private TicketMapper ticketMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalScheduleException, IllegalDateTimeFormatException, AdminNotFoundException {
    try {
      var adminId = eventInsertRequest.getAdminId();
      adminService.verifyIfExists(adminId);

      var eventToSave = eventMapper.toModel(eventInsertRequest);
      verifyIfIsValidScheduleDate(eventToSave);

      var savedEvent = eventRepository.save(eventToSave);
      return createMessageResponse(savedEvent.getId(), SAVED_MESSAGE);
    } catch (DateTimeParseException e) {
      throw new IllegalDateTimeFormatException(e);
    }
  }

  public MessageResponse saveTicket(Long eventId, TicketInsertRequest ticketInsertRequest) throws EventNotFoundException, TicketNotAvailableException {
    var attendeeToUpdate = attendeeService.verifyIfExists(ticketInsertRequest.getAttendeeId());
    var eventToAssociate = verifyIfExists(eventId);
    var ticketToSave = ticketMapper.toModel(ticketInsertRequest);
    var isPaidTicket = ticketToSave.getType().equals(TicketType.PAID);

    verifyAndDecrementTicketCount(eventToAssociate, isPaidTicket);
    verifyAndDecrementAttendeeBalance(attendeeToUpdate, eventToAssociate.getTicketPrice());

    ticketToSave.setDate(Instant.now());
    ticketToSave.setPrice(eventToAssociate.getTicketPrice());

    var savedTicket = ticketRepository.save(ticketToSave);
    attendeeToUpdate.getTickets().add(savedTicket);
    eventToAssociate.getTickets().add(savedTicket);
    eventRepository.save(eventToAssociate);

    return MessageResponse.builder()
        .message("Saved '" + savedTicket.getType() + "' Ticket with ID " + savedTicket.getId()
            + " associated with Event with ID " + eventToAssociate.getId()).build();
  }

  public MessageResponse associatePlaceById(Long eventId, Long placeId) throws EventNotFoundException, PlaceNotFoundException, EventScheduleNotAvailableException{
    var event = verifyIfExists(eventId);
    var place = placeService.verifyIfExists(placeId);
    verifyIfScheduleIsAvailable(event, place);

    event.getPlaces().add(place);
    eventRepository.save(event);

    return MessageResponse.builder()
        .message("Associated Event with ID " + event.getId() + " with Place with ID " + place.getId()).build();
  }

  /*
   * GET OPERATIONS
   */

  public Page<EventPageableResponse> findAll(Pageable pageRequest,
      String name, String description, String startDate) {
    LocalDate startDateFilter = null;
    if (!startDate.isBlank())
      try {
        startDateFilter = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      } catch (DateTimeParseException ignore) { /* will not filter by date */ }

    var entityFilter = Event.builder()
        .name(name.isBlank() ? null : name)
        .description(description.isBlank() ? null : description)
        .startDate(startDateFilter)
        .build();

    Page<Event> pagedEvents = eventRepository.pageAll(pageRequest, entityFilter);
    return pagedEvents.map(eventMapper::toEventPageableResponse);
  }

  public EventFindResponse findById(Long id) throws EventNotFoundException {
    var savedEvent = verifyIfExists(id);
    return eventMapper.toEventFindResponse(savedEvent);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(Long id) throws EventNotFoundException {
    verifyIfExists(id);
    eventRepository.deleteById(id);
    return createMessageResponse(id, DELETED_MESSAGE);
  }

  public MessageResponse deassociatePlaceById(Long eventId, Long placeId) {
    var event = verifyIfExists(eventId);
    var place = placeService.verifyIfExists(placeId);

    var places = event.getPlaces();
    if (!places.contains(place))
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Event with ID " + event.getId()
          + " is not associated with Place with ID " + place.getId());

    event.getPlaces().remove(place);
    eventRepository.save(event);

    return MessageResponse.builder()
        .message("Deassociated Event with ID " + event.getId() + " with Place with ID " + place.getId()).build();
  }

  /*
   * PUT OPERATION
  */

  public MessageResponse updateById(Long id, EventUpdateRequest eventUpdateRequest) throws EventNotFoundException, EmptyRequestException {
    var eventToUpdate = verifyIfExists(id);

    if (isBlankOrNull(eventUpdateRequest.getName())
        && eventUpdateRequest.getDescription() == null
        && eventUpdateRequest.getEmail() == null)
          throw new EmptyRequestException();

    eventToUpdate.setName(
        isBlankOrNull(eventUpdateRequest.getName())
        ? eventToUpdate.getName()
        : eventUpdateRequest.getName());

    eventToUpdate.setDescription(
        isBlankOrNull(eventUpdateRequest.getDescription())
        ? eventToUpdate.getDescription()
        : eventUpdateRequest.getDescription());

    eventToUpdate.setEmail(
        isBlankOrNull(eventUpdateRequest.getEmail())
        ? eventToUpdate.getEmail()
        : eventUpdateRequest.getEmail());

    eventRepository.save(eventToUpdate);
    return createMessageResponse(eventToUpdate.getId(), UPDATED_MESSAGE);
  }

  /*
   * METHODS
   */

  private Event verifyIfExists(Long id) throws EventNotFoundException {
    return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
  }

  private void verifyIfScheduleIsAvailable(Event event, Place place) throws EventScheduleNotAvailableException {
    List<Event> savedEvents = eventRepository.findEventsBySchedule(event, place);
    if (!savedEvents.isEmpty())
      throw new EventScheduleNotAvailableException(savedEvents.get(0), place);
  }

  private void verifyIfIsValidScheduleDate(Event event) throws IllegalScheduleException {
    var startDateTime = LocalDateTime.of(event.getStartDate(), event.getStartTime());
    var endDateTime = LocalDateTime.of(event.getEndDate(), event.getEndTime());
    var present = LocalDateTime.now();

    if (startDateTime.isBefore(present) || endDateTime.isBefore(present))
      throw new IllegalScheduleException("The Event must be scheduled in the future.");

    if (startDateTime.isAfter(endDateTime))
      throw new IllegalScheduleException("Start date-time must be before end date-time.");
  }

  private void verifyAndDecrementTicketCount(Event event, boolean isPaidTicket) throws TicketNotAvailableException {
    long ticketCount;
    if (isPaidTicket) {
      if ((ticketCount = event.getAmountPaidTicketsAvailable() - 1) < 0)
        throw new TicketNotAvailableException(isPaidTicket);
      event.setAmountPaidTicketsAvailable(ticketCount);
    } else {
      if ((ticketCount = event.getAmountFreeTicketsAvailable() - 1) < 0)
        throw new TicketNotAvailableException(isPaidTicket);
      event.setAmountFreeTicketsAvailable(ticketCount);
    }
  }

  private void verifyAndDecrementAttendeeBalance(Attendee attendee, Double priceTicket) {
    double balance;
    if ((balance = attendee.getBalance() - priceTicket) < 0)
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Insufficient funds");
    attendee.setBalance(balance);
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

  private boolean isBlankOrNull(String value) {
    return value == null || value.isBlank();
  }

}
