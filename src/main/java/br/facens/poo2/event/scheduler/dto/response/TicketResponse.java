package br.facens.poo2.event.scheduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

  private Long ticketId;
  private Long eventId;
  private Long attendeeId;
  private String date;
  private String type;
  private Double price;

}
