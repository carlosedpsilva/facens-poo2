package br.facens.poo2.event.scheduler.service;

import static br.facens.poo2.event.scheduler.utils.EventUtils.createFakeInsertRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.facens.poo2.event.scheduler.dto.mapper.EventMapper;
import br.facens.poo2.event.scheduler.dto.request.insert.EventInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.EventUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.EventPageableResponse;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import br.facens.poo2.event.scheduler.entity.Event;
import br.facens.poo2.event.scheduler.exception.admin.AdminNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.EventNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.EventScheduleNotAvailableException;
import br.facens.poo2.event.scheduler.exception.event.IllegalScheduleException;
import br.facens.poo2.event.scheduler.exception.generic.EmptyRequestException;
import br.facens.poo2.event.scheduler.exception.generic.IllegalDateTimeFormatException;
import br.facens.poo2.event.scheduler.repository.EventRepository;
import br.facens.poo2.event.scheduler.repository.TicketRepository;
import br.facens.poo2.event.scheduler.utils.EventUtils;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  private static final String SAVED_MESSAGE = "Saved Event with ID ";
  private static final String DELETED_MESSAGE = "Deleted Event with ID ";
  private static final String UPDATED_MESSAGE = "Updated Event with ID ";

  @InjectMocks
  private EventService eventService;

  // Services
  @Mock private AdminService adminService;

  // Repositories
  @Mock private EventRepository  eventRepository;
  @Mock private TicketRepository ticketRepository;

  // Mappers
  @Spy private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

  /*
   * POST OPERATION
   */

  @Test
  void testGivenValidInsertRequestThenReturnSavedMessageResponse() throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException, AdminNotFoundException {
    // given
    var eventInsertRequest = EventUtils.createFakeInsertRequest();
    var expectedSavedEvent = EventUtils.createFakeEntity();
    var expectedSavedMessageResponse = createMessageResponse(expectedSavedEvent.getId(), SAVED_MESSAGE);

    // when
    when(eventRepository.save(any(Event.class))).thenReturn(expectedSavedEvent);

    // then
    var savedMessageResponse = eventService.save(eventInsertRequest);
    Assertions.assertEquals(expectedSavedMessageResponse, savedMessageResponse);
  }

  @Test // post given invalid insert request - invalid schedule
  void testGivenInsertRequestWithInvalidScheduleThenThrowException() {
    // given
    var eventInsertRequest = EventUtils.createFakeInsertRequest();
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
    eventInsertRequest.setStartDate("Some invalid date format");

    // then
    assertThrows(IllegalDateTimeFormatException.class, () -> eventService.save(eventInsertRequest));
  }

  /*
   * GET OPERATIONS
   */

  @Test // get all registered events paged - with content
  void testGivenNoDataThenReturnAllRegisteredEventsPagedResponse() throws IllegalDateTimeFormatException {
    // given
    var pageRequest = PageRequest.of(0, 8);

    var savedEvents = Collections.singletonList(EventUtils.createFakeEntity());
    var pageableEventsResponse = Collections.singletonList(EventUtils.createFakePageableResponse());

    var expectedPagedEvents = new PageImpl<>(savedEvents, pageRequest, savedEvents.size());
    var expectedPagedEventsResponse = new PageImpl<>(pageableEventsResponse, pageRequest, pageableEventsResponse.size());

    // when
    when(eventRepository.pageAll(any(Pageable.class), any(Event.class))).thenReturn(expectedPagedEvents);

    // then
    Page<EventPageableResponse> pagedEventsResponse = eventService.findAll(pageRequest, "", "", "");
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
    Page<EventPageableResponse> pagedEvents = eventService.findAll(pageRequest, "", "", "");
    assertEquals(0, pagedEvents.getNumberOfElements());
  }

  @Test // get registered event by id
  void testGivenValidEventIdThenReturnThisEventAsResponse() throws EventNotFoundException {
    // given
    var expectedValidId = 1L;
    var expectedSavedEvent = EventUtils.createFakeEntity();
    var expectedSavedEventResponse = EventUtils.createFakeFindResponse();
    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));
    // then
    var savedEventResponse = eventService.findById(expectedValidId);
    assertEquals(expectedSavedEventResponse, savedEventResponse);
  }

  /*
   * DELETE OPERATION
   */

  @Test // delete registered event by id
  void testGivenValidEventIdThenReturnDeletedMessageResponse() throws EventNotFoundException {
    // given
    var expectedValidId = 1L;
    var expectedSavedEvent = EventUtils.createFakeEntity();
    var expectedDeletedEventResponse = createMessageResponse(expectedValidId, DELETED_MESSAGE);
    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));
    doNothing().when(eventRepository).deleteById(expectedValidId);
    // then
    var deletedEventResponse = eventService.deleteById(expectedValidId);
    verify(eventRepository, times(1)).findById(expectedValidId);
    verify(eventRepository, times(1)).deleteById(expectedValidId);
    assertEquals(expectedDeletedEventResponse, deletedEventResponse);
  }

  /*
   * PUT OPERATION
   */

  @Test // update event with valid id
  void testGivenValidUpdateRequestWithValidIdThenReturnUpdatedMessageResponse() throws EventNotFoundException, EmptyRequestException {
    // given
    var expectedValidId = 1L;
    var expectedSavedEvent = EventUtils.createFakeEntity();
    var eventUpdateRequest = EventUtils.createFakeUpdateRequest();
    var expectedUpdatedMessageResponse = createMessageResponse(expectedValidId, UPDATED_MESSAGE);
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
    var expectedSavedEvent = EventUtils.createFakeEntity();
    var invalidEventUpdateRequest = new EventUpdateRequest();
    // when
    when(eventRepository.findById(expectedValidId)).thenReturn(Optional.of(expectedSavedEvent));
    // then
    assertThrows(EmptyRequestException.class, () -> eventService.updateById(expectedValidId, invalidEventUpdateRequest));
  }

  @Test // update event with invalid id
  void testGivenUpdateRequestWithInvalidIdThenThrowException() {
    // given
    var expectedInvalidId = 1L;
    var eventUpdateRequest = EventUtils.createFakeUpdateRequest();
    // when
    when(eventRepository.findById(expectedInvalidId)).thenReturn(Optional.empty());
    // then
    assertThrows(EventNotFoundException.class, () -> eventService.updateById(expectedInvalidId, eventUpdateRequest));
  }

  /*
   * COMMON
   */

  @Test // verify if event exists by id
  void testGivenInvalidEventIdThenThrowException() {
    // given
    var expectedInvalidId = 1L;
    // when
    when(eventRepository.findById(expectedInvalidId)).thenReturn(Optional.ofNullable(any(Event.class)));
    // then
    assertThrows(EventNotFoundException.class, () -> eventService.findById(expectedInvalidId));
  }

  /*
   * METHODS
   */

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
