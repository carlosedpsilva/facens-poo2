package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.utils.EventUtils.createFakeEntity;
import static br.facens.poo2.ac2project.utils.EventUtils.createFakeFindResponse;
import static br.facens.poo2.ac2project.utils.EventUtils.createFakeInsertRequest;
import static br.facens.poo2.ac2project.utils.EventUtils.createFakePageableResponse;
import static br.facens.poo2.ac2project.utils.EventUtils.createFakeUpdateRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  private static final String SAVED_MESSAGE = "Saved Event with ID ";
  private static final String DELETED_MESSAGE = "Deleted Event with ID ";
  private static final String UPDATED_MESSAGE = "Updated Event with ID ";
  
  @InjectMocks
  private EventService eventService;
  
  @Mock
  private EventRepository eventRepository;
  
  @Spy
  private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

  // POST OPERATIONS

  @Test // post given valid insert request
  void testGivenInsertRequestThenReturnSavedMessageResponse() throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException {
    // given
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    Event eventSearchFilter = eventMapper.toModel(eventInsertRequest);
    Event expectedSavedEvent = createFakeEntity();

    MessageResponse expectedSavedMessageResponse = createMessageResponse(expectedSavedEvent.getId(), SAVED_MESSAGE);

    // when
    when(eventRepository.findEventsBySchedule(eventSearchFilter)).thenReturn(Collections.emptyList());
    when(eventRepository.save(any(Event.class))).thenReturn(expectedSavedEvent);

    // then
    MessageResponse savedMessageResponse = eventService.save(eventInsertRequest);

    Assertions.assertEquals(expectedSavedMessageResponse, savedMessageResponse);
  }

  @Test // post given valid insert request - but schedule is already registered 
  void testGivenAnAlreadyScheduledInsertRequestThenThrowException() {
    // given
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    Event expectedSavedEvent = createFakeEntity();
    
    // when
    when(eventRepository.findEventsBySchedule(any(Event.class))).thenReturn(List.of(expectedSavedEvent));
    
    // then
    assertThrows(EventScheduleNotAvailableException.class, () -> eventService.save(eventInsertRequest));
  }
  
  @Test // post given invalid insert request - invalid schedule
  void testGivenInsertRequestWithInvalidScheduleThenThrowException() {
    // given
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    var startDate = eventInsertRequest.getStartDate();

    eventInsertRequest.setStartDate(eventInsertRequest.getEndDate());
    eventInsertRequest.setEndDate(startDate);

    // then
    assertThrows(IllegalScheduleException.class, () -> eventService.save(eventInsertRequest));
  }

  @Test // post given invalid insert request - invalid date time foramt
  void testGivenInsertRequestWithInvalidDateTimeFormatThenThrowException() {
    // given
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    eventInsertRequest.setStartDate("Invalid date format");
        
    // then
    assertThrows(IllegalDateTimeFormatException.class, () -> eventService.save(eventInsertRequest));    
  }

  // GET OPERATIONS

  @Test // get all registered events paged - with content
  void testGivenNoDataThenReturnAllRegisteredEventsPagedResponse() throws IllegalDateTimeFormatException {
    // given
    PageRequest pageRequest = PageRequest.of(0, 8);

    List<Event> savedEvents = Collections.singletonList(createFakeEntity());
    List<EventPageableResponse> pageableEventsResponse = Collections.singletonList(createFakePageableResponse());
    
    Page<Event> expectedPagedEvents = new PageImpl<>(savedEvents, pageRequest, savedEvents.size());
    Page<EventPageableResponse> expectedPagedEventsResponse = new PageImpl<>(pageableEventsResponse, pageRequest, pageableEventsResponse.size());

    // when
    when(eventRepository.pageAll(any(Pageable.class), any(Event.class))).thenReturn(expectedPagedEvents);

    // then
    Page<EventPageableResponse> pagedEventsResponse = eventService.findAll(pageRequest, "", "", "", "");

    assertEquals(expectedPagedEventsResponse, pagedEventsResponse);
  }

  @Test // get all registered events paged - with no content
  void testGivenNoDataThenReturnAnEmptyPageOfEventsAsResponse() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 8);
    List<Event> emptyListOfEvents = Collections.emptyList();
    Page<Event> expectedPagedEvents = new PageImpl<>(emptyListOfEvents, pageRequest, 0);

    // when
    when(eventRepository.pageAll(any(Pageable.class), any(Event.class))).thenReturn(expectedPagedEvents);

    // then
    Page<EventPageableResponse> pagedEvents = eventService.findAll(pageRequest, "", "", "", "");
    assertTrue(pagedEvents.getNumberOfElements() == 0);
  }
  
  @Test // get registered event by id
  void testGivenValidEventIdThenReturnThisEventAsResponse() throws EventNotFoundException {
    // given 
    var expectedValidId = 1L;
    Event expectedSavedEvent = createFakeEntity();
    EventFindResponse expectedSavedEventResponse = createFakeFindResponse();

    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));
    
    // then
    EventFindResponse savedEventResponse = eventService.findById(expectedValidId);
    
    assertEquals(expectedSavedEventResponse, savedEventResponse);
  }

  // DELETE OPERATIONS
  
  @Test // delete registered event by id
  void testGivenValidEventIdThenReturnDeletedMessageResponse() throws EventNotFoundException {
    // given
    var expectedValidId = 1L;
    Event expectedSavedEvent = createFakeEntity();
    MessageResponse expectedDeletedEventResponse = createMessageResponse(expectedValidId, DELETED_MESSAGE);
    
    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));
    doNothing().when(eventRepository).deleteById(expectedValidId);
    
    // then
    MessageResponse deletedEventResponse = eventService.deleteById(expectedValidId);

    verify(eventRepository, times(1)).findById(expectedValidId);
    verify(eventRepository, times(1)).deleteById(expectedValidId);
    assertEquals(expectedDeletedEventResponse, deletedEventResponse);
  }

  // PUT OPERATIONS

  @Test // update event with valid id
  void testGivenValidUpdateRequestWithValidIdThenReturnUpdatedMessageResponse() throws EventNotFoundException, EmptyRequestException {
    // given
    var expectedValidId = 1L;
    Event expectedSavedEvent = createFakeEntity();
    EventUpdateRequest eventUpdateRequest = createFakeUpdateRequest();
    MessageResponse expectedUpdatedMessageResponse = createMessageResponse(expectedValidId, UPDATED_MESSAGE);

    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));

    // then
    MessageResponse updatedMessageResponse = eventService.updateById(expectedValidId, eventUpdateRequest);
    assertEquals(expectedUpdatedMessageResponse, updatedMessageResponse);
  }

  @Test // updated event with valid id and empty body
  void testGivenEmptyUpdateRequestWithValidIdThenThrowException() {
    // given
    var expectedValidId = 1L;
    Event expectedSavedEvent = createFakeEntity();
    EventUpdateRequest invalidEventUpdateRequest = EventUpdateRequest.builder()
        .name("")
        .description("")
        .place("")
        .emailContact("")
        .build();

    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));

    // then
    assertThrows(EmptyRequestException.class, () -> eventService.updateById(expectedValidId, invalidEventUpdateRequest));
  } 

  @Test // update event with invalid id
  void testGivenUpdateRequestWithInvalidIdThenThrowException() {
    // given
    var expectedInvalidId = 1L;
    EventUpdateRequest eventUpdateRequest = createFakeUpdateRequest();

    // when
    when(eventRepository.findById(expectedInvalidId)).thenReturn(Optional.empty());

    // then
    assertThrows(EventNotFoundException.class, () -> eventService.updateById(expectedInvalidId, eventUpdateRequest));
  }

  // COMMON METHODS

  @Test // verify if event exists by id
  void testGivenInvalidEventIdThenThrowException() {
    var expectedInvalidId = 1L;

    when(eventRepository.findById(expectedInvalidId)).thenReturn(Optional.ofNullable(any(Event.class)));

    assertThrows(EventNotFoundException.class, () -> eventService.findById(expectedInvalidId));
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
