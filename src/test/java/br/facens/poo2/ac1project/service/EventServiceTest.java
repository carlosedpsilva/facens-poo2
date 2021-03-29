package br.facens.poo2.ac1project.service;

import static br.facens.poo2.ac1project.utils.EventUtils.createFakeEntity;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakeFindResponse;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakeInsertRequest;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakePageableResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import br.facens.poo2.ac1project.dto.response.EventFindResponse;
import br.facens.poo2.ac1project.dto.response.EventPageableResponse;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.entity.Event;
import br.facens.poo2.ac1project.exception.EventAlreadyRegisteredException;
import br.facens.poo2.ac1project.exception.EventNotFoundException;
import br.facens.poo2.ac1project.exception.IllegalDateScheduleException;
import br.facens.poo2.ac1project.exception.IllegalDateTimeFormat;
import br.facens.poo2.ac1project.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
  
  @InjectMocks
  private EventService eventService;
  
  @Mock
  private EventRepository eventRepository;
  
  @Spy
  private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

  // POST

  @Test
  void testGivenInsertRequestThenReturnSavedMessage() throws IllegalDateScheduleException, IllegalDateTimeFormat, EventAlreadyRegisteredException {
    Event expectedSavedEvent = createFakeEntity();
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    MessageResponse expectedSavedMessageResponse = createMessageResponse(expectedSavedEvent.getId(), "Saved Event with ID ");

    when(eventRepository.save(any(Event.class))).thenReturn(expectedSavedEvent);

    MessageResponse savedMessageResponse = eventService.save(eventInsertRequest);

    Assertions.assertEquals(expectedSavedMessageResponse, savedMessageResponse);
  }

  @Test
  void testGivenAlreadyRegisteredInsertRequestThenThrowException() {
    Event expectedSavedEvent = createFakeEntity();
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    
    when(eventRepository.findEvent(any(Event.class))).thenReturn(Optional.of(expectedSavedEvent));
    
    assertThrows(EventAlreadyRegisteredException.class, () -> eventService.save(eventInsertRequest));
  }
  
  @Test
  void testGivenInsertRequestWithInvalidDateScheduleThenThrowException() {
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    var startDate = eventInsertRequest.getStartDate();
    eventInsertRequest.setStartDate(eventInsertRequest.getEndDate());
    eventInsertRequest.setEndDate(startDate);

    assertThrows(IllegalDateScheduleException.class, () -> eventService.save(eventInsertRequest));
  }

  @Test
  void testGivenInsertRequestWithInvalidDateTimeFormatThenThrowException() {
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();

    eventInsertRequest.setStartDate("Invalid date");
        
    assertThrows(IllegalDateTimeFormat.class, () -> eventService.save(eventInsertRequest));    
  }

  // GET

  @Test
  void testGivenNoDataThenReturnAllEventsPaged() throws IllegalDateTimeFormat {
    PageRequest pageRequest = PageRequest.of(0, 8);

    List<Event> savedEvents = Collections.singletonList(createFakeEntity());
    List<EventPageableResponse> pageableEventsResponse = Collections.singletonList(createFakePageableResponse());
    
    Page<Event> expectedPagedEvents = new PageImpl<>(savedEvents, pageRequest, savedEvents.size());
    Page<EventPageableResponse> expectedPagedEventsResponse = new PageImpl<>(pageableEventsResponse, pageRequest, pageableEventsResponse.size());

    when(eventRepository.pageAll(any(Pageable.class), any(Event.class))).thenReturn(expectedPagedEvents);

    Page<EventPageableResponse> pagedEventsResponse = eventService.findAll(pageRequest, "", "", "", "");

    assertEquals(expectedPagedEventsResponse, pagedEventsResponse);
  }
  
  @Test
  void testGivenValidEventIdThenReturnThisEvent() throws EventNotFoundException {
    var validEventId = 1L;
    Event expectedSavedEvent = createFakeEntity();
    EventFindResponse expectedSavedEventResponse = createFakeFindResponse();

    when(eventRepository.findById(validEventId)).thenReturn(Optional.of(expectedSavedEvent));

    EventFindResponse savedEventResponse = eventService.findById(validEventId);

    assertEquals(expectedSavedEventResponse, savedEventResponse);
  }

  @Test
  void testGivenInvalidEventIdThenThrowException() {
    var invalidEventId = 1L;

    when(eventRepository.findById(invalidEventId)).thenReturn(Optional.ofNullable(any(Event.class)));

    assertThrows(EventNotFoundException.class, () -> eventService.findById(invalidEventId));
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
