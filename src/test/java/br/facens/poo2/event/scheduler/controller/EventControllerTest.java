package br.facens.poo2.event.scheduler.controller;

import static br.facens.poo2.event.scheduler.utils.EventUtils.asJsonString;
import static br.facens.poo2.event.scheduler.utils.EventUtils.createFakeFindResponse;
import static br.facens.poo2.event.scheduler.utils.EventUtils.createFakeInsertRequest;
import static br.facens.poo2.event.scheduler.utils.EventUtils.createFakePageableResponse;
import static br.facens.poo2.event.scheduler.utils.EventUtils.createFakeUpdateRequest;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.facens.poo2.event.scheduler.dto.request.insert.EventInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.EventUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.EventPageableResponse;
import br.facens.poo2.event.scheduler.dto.response.EventResponse;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import br.facens.poo2.event.scheduler.exception.event.EventNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.IllegalScheduleException;
import br.facens.poo2.event.scheduler.exception.generic.EmptyRequestException;
import br.facens.poo2.event.scheduler.service.EventService;
import br.facens.poo2.event.scheduler.service.TicketService;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

  private static final String EVENT_API_URL_PATH = "/api/v1/events";
  private static final String SAVED_MESSAGE = "Saved Event with ID ";
  private static final String DELETED_MESSAGE = "Deleted Event with ID ";
  private static final String UPDATED_MESSAGE = "Updated Event with ID ";

  private MockMvc mockMvc;

  private EventController eventController;

  @Mock private EventService eventService;
  @Mock private TicketService ticketService;

  @BeforeEach
  void setUp() {
    eventController = new EventController(eventService, ticketService);
    mockMvc = MockMvcBuilders.standaloneSetup(eventController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
        .build();
  }

  /*
   * POST OPERATION
   */

  @Test // post given valid insert request
  void testWhenPOSTIsCalledThenAnEventScheduleShouldBeSavedWithCreatedStatus() throws Exception {
    // given
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    MessageResponse expectedMessageResponse = createMessageResponse(1L, SAVED_MESSAGE);

    // when
    when(eventService.save(eventInsertRequest)).thenReturn(expectedMessageResponse);

    // then
    mockMvc.perform(post(EVENT_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(eventInsertRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", is(expectedMessageResponse.getMessage())));
  }

  @Test // post given valid insert request - but schedule is already registered
  void testWhenPOSTIsCalledWithInvalidOrMissingFieldsThenReturnBadRequestStatus() throws Exception {
    // given
    EventInsertRequest invalidInsertRequest = createFakeInsertRequest();

    // when
    when(eventService.save(invalidInsertRequest)).thenThrow(IllegalScheduleException.class);

    // then
    assertThrows(IllegalScheduleException.class, () -> eventService.save(invalidInsertRequest));

    mockMvc.perform(post(EVENT_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(invalidInsertRequest)))
        .andExpect(status().isBadRequest());
  }

  /*
   * GET OPERATIONS
   */

  @Test // get all registered events paged
  void testWhenGETIsCalledThenReturnAllSavedEventsPagedWithOkStatus() throws Exception {
    // given
    PageRequest pageRequest = PageRequest.of(0, 8);
    List<EventPageableResponse> pageableEventsResponse = Collections.singletonList(createFakePageableResponse());
    Page<EventPageableResponse> expectedPagedEventsResponse = new PageImpl<>(pageableEventsResponse, pageRequest, pageableEventsResponse.size());

    // when
    when(eventService.findAll(any(Pageable.class), eq(""), eq(""), eq(""))).thenReturn(expectedPagedEventsResponse);

    Page<EventPageableResponse> pagedEventsResponse = eventService.findAll(pageRequest, "", "", "");

    // then
    assertEquals(1, pagedEventsResponse.getNumberOfElements());
    assertEquals(expectedPagedEventsResponse, pagedEventsResponse);

    mockMvc.perform(get(EVENT_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].eventId", is(1)));
  }

  @Test // get registered event with id
  void testWhenGETIsCalledWithValidEventIdThenCorrespondingEventWithOkStatus() throws Exception {
    // given
    var expectedValidId = 1L;
    EventResponse expectedEventFindResponse = createFakeFindResponse();

    // whem
    when(eventService.findById(expectedValidId)).thenReturn(expectedEventFindResponse);

    // then
    mockMvc.perform(get(EVENT_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eventId", is(1)));
  }

  @Test // get registered event with invalid id
  void testWhenGETIsCalledWithInvalidEventIdThenNotFoundStatus() throws Exception {
    // given
    var expectedInvalidId = 1L;

    // when
    when(eventService.findById(expectedInvalidId)).thenThrow(EventNotFoundException.class);

    // then
    mockMvc.perform(get(EVENT_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  /*
   * DELETE OPERATION
   */

  @Test // delete registered event with valid id
  void testWhenDELETEIsCalledWithValidEventIdThenReturnOkStatus() throws Exception {
    // given
    var expectedValidId = 1L;
    MessageResponse expectedMessageResponse = createMessageResponse(expectedValidId, DELETED_MESSAGE);

    // when
    when(eventService.deleteById(expectedValidId)).thenReturn(expectedMessageResponse);

    // then
    mockMvc.perform(delete(EVENT_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", is(expectedMessageResponse.getMessage())));
  }

  @Test // delete registered event with invalid id
  void testWhenDELETEIsCalledWithInvalidEventIdThenReturnNotFoundStatus() throws Exception {
    // given
    var expectedInvalidId = 1L;

    // when
    doThrow(EventNotFoundException.class).when(eventService).deleteById(expectedInvalidId);

    // then
    mockMvc.perform(delete(EVENT_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  /*
   * PUT OPERATION
   */

  @Test // update registered event with valid id
  void testWhenPUTIsCalledWithValidUpdateRequestThenReturnOkStatus() throws Exception {
    // given
    var expectedValidId = 1L;
    EventUpdateRequest eventUpdateRequest = createFakeUpdateRequest();
    MessageResponse expectedUpdatedMessageResponse = createMessageResponse(expectedValidId, UPDATED_MESSAGE);

    // when
    when(eventService.updateById(expectedValidId, eventUpdateRequest)).thenReturn(expectedUpdatedMessageResponse);

    // then
    mockMvc.perform(put(EVENT_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(eventUpdateRequest)))
        .andExpect(status().isOk());
  }

  @Test // update registered event with valid id by empty update request
  void testWhenPUTIsCalledWithEmptyUpdateRequestThenReturnOkStatus() throws Exception {
    // given
    var expectedValidId = 1L;
    EventUpdateRequest invalidEventUpdateRequest = createFakeUpdateRequest();

    // when
    when(eventService.updateById(expectedValidId, invalidEventUpdateRequest)).thenThrow(EmptyRequestException.class);

    // then
    mockMvc.perform(put(EVENT_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(invalidEventUpdateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test // update registered event with invalid id
  void testWhenPUTIsCalledWithInvalidUpdateRequestThenReturnNotFoundStatus() throws Exception {
    // given
    var expectedInvalidId = 1L;
    EventUpdateRequest eventUpdateRequest = createFakeUpdateRequest();

    // when
    doThrow(EventNotFoundException.class).when(eventService).updateById(expectedInvalidId, eventUpdateRequest);

    // then
    mockMvc.perform(put(EVENT_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(eventUpdateRequest)))
        .andExpect(status().isNotFound());
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
