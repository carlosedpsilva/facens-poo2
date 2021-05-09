package br.facens.poo2.ac2project.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.EventMapper;
import br.facens.poo2.ac2project.dto.request.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.response.EventFindResponse;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.EventNotFoundException;
import br.facens.poo2.ac2project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.IllegalScheduleException;
import br.facens.poo2.ac2project.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private static final String SAVED_MESSAGE = "Saved Event with ID ";
  private static final String DELETED_MESSAGE = "Deleted Event with ID ";
  private static final String UPDATED_MESSAGE = "Updated Event with ID ";

  private EventRepository eventRepository;

  private EventMapper eventMapper;

  // POST 

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException {
    try {
      Event eventToSave = eventMapper.toModel(eventInsertRequest);
      verifyIfIsValidScheduleDate(eventToSave);
      verifyIfScheduleIsAvailable(eventToSave);

      Event savedEvent = eventRepository.save(eventToSave);
      return createMessageResponse(savedEvent.getId(), SAVED_MESSAGE);
    } catch (DateTimeParseException e) {
      throw new IllegalDateTimeFormatException(e);
    }
  }
  
  // GET
  
  public Page<EventPageableResponse> findAll(Pageable pageRequest,
      String name, String description, String place, String startDate) {

    LocalDate startDateFilter = null;

    if (!startDate.isBlank())
      try {
        startDateFilter = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      } catch (DateTimeParseException e) {
        // do nothing
      }

    Event entityFilter = Event.builder()
        .name(               name.isBlank() ? null : name )
        // .place(             place.isBlank() ? null : place )
        .description( description.isBlank() ? null : description )
        .startDate( startDateFilter )
        .build();

    Page<Event> pagedEvents = eventRepository.pageAll(pageRequest, entityFilter);
    return pagedEvents.map(eventMapper::toEventPageableResponse);
  }
  
  public EventFindResponse findById(Long id) throws EventNotFoundException {
    Event savedEvent = verifyIfExists(id);
    return eventMapper.toEventFindResponse(savedEvent);
  }

  // DELETE

  public MessageResponse deleteById(Long id) throws EventNotFoundException {
    verifyIfExists(id);
    eventRepository.deleteById(id);
    return createMessageResponse(id, DELETED_MESSAGE);
  }

  // PUT

  public MessageResponse updateById(Long id, EventUpdateRequest eventUpdateRequest) throws EventNotFoundException, EmptyRequestException {
    Event eventToUpdate = verifyIfExists(id);

    if (eventUpdateRequest.getName().isEmpty()
        && eventUpdateRequest.getDescription().isEmpty()
        && eventUpdateRequest.getPlace().isEmpty()
        && eventUpdateRequest.getEmail().isEmpty())
          throw new EmptyRequestException();

    eventToUpdate.setName(eventUpdateRequest.getName().isEmpty() ? eventToUpdate.getName() : eventUpdateRequest.getName());
    eventToUpdate.setDescription(eventUpdateRequest.getDescription().isEmpty() ? eventToUpdate.getDescription() : eventUpdateRequest.getDescription());
    // eventToUpdate.setPlace(eventUpdateRequest.getPlace().isEmpty() ? eventToUpdate.getPlace() : eventUpdateRequest.getPlace());
    // eventToUpdate.setEmail(eventUpdateRequest.getEmail().isEmpty() ? eventToUpdate.getEmail() : eventUpdateRequest.getEmail());

    eventRepository.save(eventToUpdate);
    return createMessageResponse(eventToUpdate.getId(), UPDATED_MESSAGE);
  }

  // METHODS

  private Event verifyIfExists(Long id) throws EventNotFoundException {
    return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
  }

  private void verifyIfScheduleIsAvailable(Event eventToSave) throws EventScheduleNotAvailableException {
    List<Event> savedEvents = eventRepository.findEventsBySchedule(eventToSave);
    if (!savedEvents.isEmpty())
      throw new EventScheduleNotAvailableException(savedEvents.get(0));
  }

  private void verifyIfIsValidScheduleDate(Event event) throws IllegalScheduleException {
    if (event.getStartDate().isAfter(event.getEndDate()))
      throw new IllegalScheduleException("Could not register event. Specified start date cannot be after end date.");
    if (event.getStartDate().isEqual(event.getEndDate()) && event.getStartTime().isAfter(event.getEndTime())) 
      throw new IllegalScheduleException("Could not register event. Specified start time cannot be after end time");
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
