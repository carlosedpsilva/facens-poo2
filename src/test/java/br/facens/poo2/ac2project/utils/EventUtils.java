package br.facens.poo2.ac2project.utils;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.facens.poo2.ac2project.dto.request.insert.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.response.EventResponse;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.entity.Event;

public class EventUtils {

  private static Long      EVENT_ID                      = 1L;
  private static Long      ADMIN_ID                      = 1L;
  private static LocalDate ENTITY_START_DATE             = LocalDate.of(2022, 03, 11);
  private static LocalDate ENTITY_END_DATE               = LocalDate.of(2022, 03, 15);
  private static LocalTime ENTITY_START_TIME             = LocalTime.of(11, 00);
  private static LocalTime ENTITY_END_TIME               = LocalTime.of(15, 00);
  private static String    NAME                          = "Fake name";
  private static String    DESCRIPTION                   = "Fake description";
  private static String    STRING_START_DATE             = "11/03/2022";
  private static String    STRING_END_DATE               = "15/03/2022";
  private static String    STRING_START_TIME             = "11:00";
  private static String    STRING_END_TIME               = "15:00";
  private static String    EMAIL                         = "example@email.com";
  private static Long      AMOUNT_FREE_TICKETS_AVAILABLE = 200L;
  private static Long      AMOUND_PAID_TICKETS_AVAILABLE = 500L;
  private static Long      AMOUNT_FREE_TICKETS_SOLD      = 0L;
  private static Long      AMOUND_PAID_TICKETS_SOLD      = 0L;
  private static Double    TICKET_PRICE                  = 100.0;


  public static Event createFakeEntity() {
    return Event.builder()
        .id(EVENT_ID)
        .admin(AdminUtils.createFakeEntity())
        .name(NAME)
        .description(DESCRIPTION)
        .startDate(ENTITY_START_DATE)
        .endDate(ENTITY_END_DATE)
        .startTime(ENTITY_START_TIME)
        .endTime(ENTITY_END_TIME)
        .email(EMAIL)
        .amountFreeTicketsAvailable(AMOUNT_FREE_TICKETS_AVAILABLE)
        .amountPaidTicketsAvailable(AMOUND_PAID_TICKETS_AVAILABLE)
        .amountFreeTicketsSold(AMOUNT_FREE_TICKETS_SOLD)
        .amountPaidTicketsSold(AMOUND_PAID_TICKETS_SOLD)
        .ticketPrice(TICKET_PRICE)
        .build();
  }

  /*
   * Request
   */

  public static EventInsertRequest createFakeInsertRequest() {
    return EventInsertRequest.builder()
        .name(NAME)
        .adminId(ADMIN_ID)
        .description(DESCRIPTION)
        .startDate(STRING_START_DATE)
        .endDate(STRING_END_DATE)
        .startTime(STRING_START_TIME)
        .endTime(STRING_END_TIME)
        .email(EMAIL)
        .amountFreeTickets(AMOUNT_FREE_TICKETS_AVAILABLE)
        .amountPaidTickets(AMOUND_PAID_TICKETS_AVAILABLE)
        .ticketPrice(TICKET_PRICE)
        .build();
  }


  public static EventUpdateRequest createFakeUpdateRequest() {
    return EventUpdateRequest.builder()
        .name(NAME)
        .description(DESCRIPTION)
        .email(EMAIL)
        .build();
  }

  /*
   * Response
   */

  public static EventResponse createFakeFindResponse() {
    return EventResponse.builder()
        .eventId(EVENT_ID)
        .adminId(ADMIN_ID)
        .name(NAME)
        .description(DESCRIPTION)
        .startDate(STRING_START_DATE)
        .endDate(STRING_END_DATE)
        .startTime(STRING_START_TIME)
        .endTime(STRING_END_TIME)
        .email(EMAIL)
        .amountFreeTicketsAvailable(AMOUNT_FREE_TICKETS_AVAILABLE)
        .amountPaidTicketsAvailable(AMOUND_PAID_TICKETS_AVAILABLE)
        .amountFreeTicketsSold(AMOUNT_FREE_TICKETS_SOLD)
        .amountPaidTicketsSold(AMOUND_PAID_TICKETS_SOLD)
        .ticketPrice(TICKET_PRICE)
        .build();
  }

  public static EventPageableResponse createFakePageableResponse() {
    return EventPageableResponse.builder()
        .eventId(EVENT_ID)
        .adminId(ADMIN_ID)
        .name(NAME)
        .description(DESCRIPTION)
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
