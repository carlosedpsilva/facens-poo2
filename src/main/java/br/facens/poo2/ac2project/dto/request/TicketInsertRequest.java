package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketInsertRequest {

  @Positive(message = "Invalid Attendee ID")
  @Digits(integer = 19, fraction = 0, message = "Invalid Attendee ID")
  private Long attendeeId;

  @Pattern(regexp = "^(?:FREE|PAID)$", message = "Ticket type must be either FREE OR PAID")
  private String type;

}
