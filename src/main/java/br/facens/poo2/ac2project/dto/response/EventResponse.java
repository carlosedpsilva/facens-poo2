package br.facens.poo2.ac2project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

  private Long eventId;
  private Long adminId;
  private String name;
  private String description;
  private String startDate;
  private String endDate;
  private String startTime;
  private String endTime;
  private String email;
  private Long amountFreeTicketsAvailable;
  private Long amountPaidTicketsAvailable;
  private Long amountFreeTicketsSold;
  private Long amountPaidTicketsSold;
  private Double ticketPrice;

}
