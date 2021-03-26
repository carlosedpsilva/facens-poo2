package br.facens.poo2.ac1project.service;

import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac1project.dto.mapper.EventMapper;
import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.EventFindResponse;
import br.facens.poo2.ac1project.dto.response.EventPageableResponse;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.exception.EventAlreadyRegisteredException;
import br.facens.poo2.ac1project.exception.EventNotFoundException;
import br.facens.poo2.ac1project.exception.IllegalDateScheduleException;
import br.facens.poo2.ac1project.exception.IllegalEventDateTimeFormat;
import br.facens.poo2.ac1project.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private EventRepository eventRepository;

  private EventMapper eventMapper;

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalDateScheduleException, IllegalEventDateTimeFormat, EventAlreadyRegisteredException {
    try {
      Event eventToSave = eventMapper.toModel(eventInsertRequest);
      isValidDateSchedule(eventToSave);
      verifyIfIsAlreadyRegistered(eventToSave);

      Event savedEvent = eventRepository.save(eventToSave);
      return createMessageResponse(savedEvent.getId(), "Saved Event with ID ");
    } catch (DateTimeParseException e) {
      throw new IllegalEventDateTimeFormat(e);
    }
  }
  
  public Page<EventPageableResponse> findAll(PageRequest pageRequest) {
    Page<Event> pagedEvents = eventRepository.pageAll(pageRequest);
    return pagedEvents.map(eventMapper::toEventPageableResponse);
  }
  
  public EventFindResponse findById(Long id) throws EventNotFoundException {
    Event savedEvent = verifyIfExists(id);
    return eventMapper.toEventFindResponse(savedEvent);
  }

  private Event verifyIfExists(Long id) throws EventNotFoundException {
    return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
  }

  private void verifyIfIsAlreadyRegistered(Event eventToSave) throws EventAlreadyRegisteredException {
    if (eventRepository.findEvent(eventToSave).isPresent())
      throw new EventAlreadyRegisteredException(eventToSave.getName());
  }

  private void isValidDateSchedule(Event event) throws IllegalDateScheduleException {
    if (event.getStartDate().isAfter(event.getEndDate()))
      throw new IllegalDateScheduleException("Could not register event. Event start date is after end date.");
    if (event.getStartDate().isEqual(event.getEndDate()) && event.getStartTime().isAfter(event.getEndTime())) 
      throw new IllegalDateScheduleException("Could not register event. Event start time is after end time");
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
