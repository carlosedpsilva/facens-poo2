package br.facens.poo2.ac1project.utils;

import java.time.LocalDate;
import java.time.LocalTime;

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
  private static LocalDate START_DATE = LocalDate.of(2022, 03, 25);
  private static LocalDate END_DATE = LocalDate.of(2022, 03, 31);
  private static LocalTime START_TIME = LocalTime.of(14, 00);
  private static LocalTime END_TIME = LocalTime.of(18, 00);
  private static String EMAIL = "example@email.com";

  public static Event createFakeEntity() {
    return Event.builder()
        .id(EVENT_ID)
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate(START_DATE)
        .endDate(END_DATE)
        .startTime(START_TIME)
        .endTime(END_TIME)
        .email(EMAIL)
        .build();
  }

  // Request

  public static EventInsertRequest createFakeInsertRequest() {
    return EventInsertRequest.builder()
        .name(NAME)
        .description(DESCRIPTION)
        .place(PLACE)
        .startDate("25/03/2022")
        .endDate("31/03/2022")
        .startTime("14:00")
        .endTime("18:00")
        .email(EMAIL)
        .build();
  }
    
  public static EventUpdateRequest createFakeUpdateRequest() {
    return EventUpdateRequest.builder()
        .id(EVENT_ID)
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
        .startDate("25/03/2022")
        .endDate("31/03/2022")
        .startTime("14:00")
        .endTime("18:00")
        .email(EMAIL)
        .build();
  }

  public static EventPageableResponse createFakePageableResponse() {
    return EventPageableResponse.builder()
        .id(EVENT_ID)
        .name(NAME)
        .place(PLACE)
        .startDate("25/03/2022")
        .startTime("14:00")
        .build();
  }

}
