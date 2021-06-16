package br.facens.poo2.ac2project.dto.info;

import java.time.Instant;

public interface TicketComponentInfo {

  Long getTicketId();

  Long getEventId();

  Long getAttendeeId();

  Instant getDate();

  String getType();

  Double getPrice();

}
