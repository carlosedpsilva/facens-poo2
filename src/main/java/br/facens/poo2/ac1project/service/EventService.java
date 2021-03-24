package br.facens.poo2.ac1project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac1project.dto.mapper.EventMapper;
import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.repository.EventRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {

  private EventRepository eventRepository;

  private EventMapper eventMapper;

  public MessageResponse save(EventInsertRequest eventInsertRequest) {
    Event eventToSave = eventMapper.toModel(eventInsertRequest);
    Event savedEvent = eventRepository.save(eventToSave);
    return createMessageResponse(savedEvent.getId(), "Saved Event with ID ");
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
