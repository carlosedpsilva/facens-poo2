package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.util.SchedulerUtils.ASSOCIATED_OPEATION_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.ASSOCIATION_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.BASIC_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.ADMIN;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.EVENT;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.PLACE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.ASSOCIATED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DEASSOCIATED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.SAVED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.UPDATED;

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
import br.facens.poo2.ac2project.dto.request.insert.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.dto.response.EventResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Place;
import br.facens.poo2.ac2project.exception.admin.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.event.EventNotFoundException;
import br.facens.poo2.ac2project.exception.event.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.event.IllegalScheduleException;
import br.facens.poo2.ac2project.exception.generic.EmptyRequestException;
import br.facens.poo2.ac2project.exception.generic.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.place.PlaceNotFoundException;
import br.facens.poo2.ac2project.repository.EventRepository;
import br.facens.poo2.ac2project.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventService implements SchedulerService<Event> {

  private final AdminService adminService;
  private final PlaceService placeService;

  private final EventRepository eventRepository;

  private final EventMapper eventMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalScheduleException, IllegalDateTimeFormatException, AdminNotFoundException {
    try {
      var adminId = eventInsertRequest.getAdminId();
      var adminToAssociate = adminService.verifyIfExists(adminId);

      var eventToSave = eventMapper.toModel(eventInsertRequest);
      verifyIfIsValidScheduleDate(eventToSave);

      eventToSave.setAdmin(adminToAssociate);
      var savedEvent = eventRepository.save(eventToSave);
      return createMessageResponse(ASSOCIATED_OPEATION_MESSAGE, SAVED, EVENT, savedEvent.getId(),
          ASSOCIATED, ADMIN, adminToAssociate.getId());
    } catch (DateTimeParseException e) {
      throw new IllegalDateTimeFormatException(e);
    }
  }

  public MessageResponse associatePlaceById(long eventId, long placeId) throws EventNotFoundException, PlaceNotFoundException, EventScheduleNotAvailableException{
    var eventToUpdate = verifyIfExists(eventId);
    var placeToAssociate = placeService.verifyIfExists(placeId);
    verifyIfScheduleIsAvailable(eventToUpdate, placeToAssociate);

    eventToUpdate.getPlaces().add(placeToAssociate);
    eventRepository.save(eventToUpdate);

    return createMessageResponse(ASSOCIATION_MESSAGE, ASSOCIATED,
        EVENT, eventToUpdate.getId(), PLACE, placeToAssociate.getId());
  }

  /*
   * GET OPERATION
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

  public EventResponse findById(Long id) throws EventNotFoundException {
    var savedEvent = verifyIfExists(id);
    return eventMapper.toEventFindResponse(savedEvent);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(long id) throws EventNotFoundException {
    var eventToDelete = verifyIfExists(id);
    var associatedAdmin = eventToDelete.getAdmin();
    eventRepository.deleteById(id);
    return createMessageResponse(ASSOCIATED_OPEATION_MESSAGE, DELETED, EVENT, eventToDelete.getId(),
        ASSOCIATED, ADMIN, associatedAdmin.getId());
  }

  public MessageResponse deassociatePlaceById(long eventId, long placeId) {
    var eventToUpdate = verifyIfExists(eventId);
    var placeToDeassociate = placeService.verifyIfExists(placeId);

    var places = eventToUpdate.getPlaces();
    if (!places.contains(placeToDeassociate))
      throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Event with ID " + eventToUpdate.getId()
          + " is not associated with Place with ID " + placeToDeassociate.getId());

    eventToUpdate.getPlaces().remove(placeToDeassociate);
    eventRepository.save(eventToUpdate);

    return createMessageResponse(ASSOCIATION_MESSAGE, DEASSOCIATED,
        EVENT, eventToUpdate.getId(), PLACE, placeToDeassociate.getId());
  }

  /*
   * PUT OPERATION
  */

  public MessageResponse updateById(long id, EventUpdateRequest eventUpdateRequest) throws EventNotFoundException, EmptyRequestException {
    var eventToUpdate = verifyIfExists(id);

    if (eventUpdateRequest.getName().isBlank()
        && eventUpdateRequest.getDescription().isBlank()
        && eventUpdateRequest.getEmail().isBlank())
      throw new EmptyRequestException();

    eventToUpdate.setName(
        eventUpdateRequest.getName().isBlank()
        ? eventToUpdate.getName()
        : eventUpdateRequest.getName());

    eventToUpdate.setDescription(
        eventUpdateRequest.getDescription().isBlank()
        ? eventToUpdate.getDescription()
        : eventUpdateRequest.getDescription());

    eventToUpdate.setEmail(
        eventUpdateRequest.getEmail().isBlank()
        ? eventToUpdate.getEmail()
        : eventUpdateRequest.getEmail());

    eventRepository.save(eventToUpdate);
    return createMessageResponse(BASIC_MESSAGE, UPDATED, EVENT, eventToUpdate.getId());
  }

  /*
   * OTHER
   */

  @Override
  public Event verifyIfExists(long id) throws EventNotFoundException {
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

}
