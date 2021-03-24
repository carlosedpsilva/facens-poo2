package br.facens.poo2.ac1project.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.facens.poo2.ac1project.dto.mapper.EventMapper;
import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.repository.EventRepository;
import br.facens.poo2.ac1project.utils.EventUtils;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
  
  @InjectMocks
  private EventService eventService;
  
  @Mock
  private EventRepository eventRepository;
  
  @Spy
  private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

  @Test
  void testGivenInsertRequestThenReturnSavedMessage() {
    Event expectedSavedEvent = EventUtils.createFakeEntity();
    EventInsertRequest eventInsertRequest = EventUtils.createFakeInsertRequest();
    MessageResponse expectedSavedMessageResponse = createMessageResponse(expectedSavedEvent.getId(), "Saved Event with ID ");

    when(eventRepository.save(any(Event.class))).thenReturn(expectedSavedEvent);

    MessageResponse savedMessageResponse = eventService.save(eventInsertRequest);

    Assertions.assertEquals(expectedSavedMessageResponse, savedMessageResponse);
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }
}
