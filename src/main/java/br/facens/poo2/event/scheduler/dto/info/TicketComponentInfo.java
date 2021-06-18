package br.facens.poo2.event.scheduler.dto.info;

import java.time.Instant;

public interface TicketComponentInfo {

  Long getTicketId();

  Long getEventId();

  Long getAttendeeId();

  Instant getDate();

  String getType();

  Double getPrice();

}
