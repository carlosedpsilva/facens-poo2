package br.facens.poo2.ac1project.service;

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
import br.facens.poo2.ac1project.execption.EventNotFoundException;
import br.facens.poo2.ac1project.execption.IllegalDateScheduleException;
import br.facens.poo2.ac1project.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private EventRepository eventRepository;

  private EventMapper eventMapper;

  public MessageResponse save(EventInsertRequest eventInsertRequest) throws IllegalDateScheduleException {
    Event eventToSave = eventMapper.toModel(eventInsertRequest);
    if (!isValidDate(eventToSave)) throw new IllegalDateScheduleException();
    
    Event savedEvent = eventRepository.save(eventToSave);
    return createMessageResponse(savedEvent.getId(), "Saved Event with ID ");
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

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

  private boolean isValidDate(Event event) {
    return event.getStartDate().isBefore(event.getEndDate())
        || event.getStartDate().isEqual(event.getEndDate())
        && event.getStartTime().isBefore(event.getEndTime());
  }

}
