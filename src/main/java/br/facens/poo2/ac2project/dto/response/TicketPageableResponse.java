package br.facens.poo2.ac2project.dto.response;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPageableResponse {

  private Long eventId;
  private Double ticketPrice;
  private Long amountFreeTicketsAvailable;
  private Long amountPaidTicketsAvailable;
  private Long amountFreeTicketsSold;
  private Long amountPaidTicketsSold;

  private Page<TicketPageComponentResponse> tickets;

}
