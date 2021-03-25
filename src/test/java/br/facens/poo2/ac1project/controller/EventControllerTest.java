package br.facens.poo2.ac1project.controller;

import static br.facens.poo2.ac1project.utils.EventUtils.asJsonString;
import static br.facens.poo2.ac1project.utils.EventUtils.createFakeInsertRequest;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.response.MessageResponse;
import br.facens.poo2.ac1project.service.EventService;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
  
  private static final String EVENT_API_URL_PATH = "/api/v1/events";

  private MockMvc mockMvc;

  private EventController eventController;

  @Mock
  private EventService eventService;

  @BeforeEach
  void setUp() {
    eventController = new EventController(eventService);
    mockMvc = MockMvcBuilders.standaloneSetup(eventController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
        .build();
  }

  @Test
  void testWhenPOSTIsCalledThenAnEventShouldBeCreated() throws Exception {
    EventInsertRequest eventInsertRequest = createFakeInsertRequest();
    MessageResponse expectedMessageResponse = createMessageResponse(1L, "Saved Event with ID ");

    when(eventService.save(eventInsertRequest)).thenReturn(expectedMessageResponse);

    mockMvc.perform(post(EVENT_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(eventInsertRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", is(expectedMessageResponse.getMessage())));
  }

  private MessageResponse createMessageResponse(Long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
