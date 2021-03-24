package br.facens.poo2.ac1project.service;

import static br.facens.poo2.ac1project.utils.EventUtils.createFakeEntity;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakeInsertRequest;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakePageableResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.facens.poo2.ac1project.dto.mapper.EventMapper;
import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.EventPageableResponse;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.repository.EventRepository;

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
    Event expectedSavedEvent = createFakeEntity();
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    MessageResponse expectedSavedMessageResponse = createMessageResponse(expectedSavedEvent.getId(), "Saved Event with ID ");

    when(eventRepository.save(any(Event.class))).thenReturn(expectedSavedEvent);

    MessageResponse savedMessageResponse = eventService.save(eventInsertRequest);

    Assertions.assertEquals(expectedSavedMessageResponse, savedMessageResponse);
  }

  @Test
  void testGivenNoDataThenReturnAllEventsPaged() {
    PageRequest pageRequest = PageRequest.of(0, 8);

    List<Event> savedEvents = Collections.singletonList(createFakeEntity());
    List<EventPageableResponse> pageableEventsResponse = Collections.singletonList(createFakePageableResponse());
    
    Page<Event> expectedPagedEvents = new PageImpl<>(savedEvents, pageRequest, savedEvents.size());
    Page<EventPageableResponse> expectedPagedEventsResponse = new PageImpl<>(pageableEventsResponse, pageRequest, pageableEventsResponse.size());

    when(eventRepository.pageAll(any(Pageable.class))).thenReturn(expectedPagedEvents);

    Page<EventPageableResponse> pagedEventsResponse = eventService.findAll(pageRequest);

    assertEquals(expectedPagedEventsResponse, pagedEventsResponse);
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
