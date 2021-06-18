package br.facens.poo2.event.scheduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPageComponentResponse {

  private Long ticketId;
  private Long attendeeId;
  private String attendeeName;
  private String type;

}
