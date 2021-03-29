package br.facens.poo2.ac1project.utils;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.facens.poo2.ac1project.dto.request.EventInsertRequest;
import br.facens.poo2.ac1project.dto.request.EventUpdateRequest;
import br.facens.poo2.ac1project.dto.response.EventFindResponse;
import br.facens.poo2.ac1project.dto.response.EventPageableResponse;
import br.facens.poo2.ac1project.entity.Event;

public class EventUtils {

  private static Long EVENT_ID = 1L;
  private static String NAME = "Fake name";
  private static String DESCRIPTION = "Fake description";
  private static String PLACE = "Fake place";
  private static LocalDate ENTITY_START_DATE = LocalDate.of(2022, 03, 11);
  private static LocalDate ENTITY_END_DATE = LocalDate.of(2022, 03, 15);
  private static LocalTime ENTITY_START_TIME = LocalTime.of(11, 00);
  private static LocalTime ENTITY_END_TIME = LocalTime.of(15, 00);
  private static String STRING_START_DATE = "11/03/2022";
  private static String STRING_END_DATE = "15/03/2022";
  private static String STRING_START_TIME = "11:00";
  private static String STRING_END_TIME = "15:00";
  private static String EMAIL = "example@email.com";

  public static Event createFakeEntity() {
    return Event.builder()
        .id(EVENT_ID)
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate(ENTITY_START_DATE)
        .endDate(ENTITY_END_DATE)
        .startTime(ENTITY_START_TIME)
        .endTime(ENTITY_END_TIME)
        .email(EMAIL)
        .build();
  }

  // Request

  public static EventInsertRequest createFakeInsertRequest() {
    return EventInsertRequest.builder()
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate(STRING_START_DATE)
        .endDate(STRING_END_DATE)
        .startTime(STRING_START_TIME)
        .endTime(STRING_END_TIME)
        .email(EMAIL)
        .build();
  }
    
  public static EventUpdateRequest createFakeUpdateRequest() {
    return EventUpdateRequest.builder()
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .email(EMAIL)
        .build();
  }

  // Response

  public static EventFindResponse createFakeFindResponse() {
    return EventFindResponse.builder()
        .id(EVENT_ID)
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate(STRING_START_DATE)
        .endDate(STRING_END_DATE)
        .startTime(STRING_START_TIME)
        .endTime(STRING_END_TIME)
        .email(EMAIL)
        .build();
  }

  public static EventPageableResponse createFakePageableResponse() {
    return EventPageableResponse.builder()
        .id(EVENT_ID)
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate(STRING_START_DATE)
        .startTime(STRING_START_TIME)
        .build();
  }

  public static <T> String asJsonString(T responseObject) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      
      return objectMapper.writeValueAsString(responseObject);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
