package br.facens.poo2.ac1project.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac1project.dto.mapper.EventMapper;
import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.EventFindResponse;
import br.facens.poo2.ac1project.dto.response.EventPageableResponse;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac1project.exception.EventNotFoundException;
import br.facens.poo2.ac1project.exception.IllegalScheduleException;
import br.facens.poo2.ac1project.exception.IllegalDateTimeFormatException;
import br.facens.poo2.ac1project.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private EventRepository eventRepository;

  private EventMapper eventMapper;

  // POST 

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException {
    try {
      Event eventToSave = eventMapper.toModel(eventInsertRequest);
      verifyIfIsValidScheduleDate(eventToSave);
      verifyIfScheduleIsAvailable(eventToSave);

      Event savedEvent = eventRepository.save(eventToSave);
      return createMessageResponse(savedEvent.getId(), "Saved Event with ID ");
    } catch (DateTimeParseException e) {
      throw new IllegalDateTimeFormatException(e);
    }
  }
  
  // GET
  
  public Page<EventPageableResponse> findAll(Pageable pageRequest,
      String name, String place, String description, String startDate) throws IllegalDateTimeFormatException {

    LocalDate startDateFilter = null;

    if (!startDate.isBlank())
      try {
        startDateFilter = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      } catch (DateTimeParseException e) {
        // do nothing
      }

    Event entityFilter = Event.builder()
        .name(               name.isBlank() ? null : name )
        .place(             place.isBlank() ? null : place )
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
