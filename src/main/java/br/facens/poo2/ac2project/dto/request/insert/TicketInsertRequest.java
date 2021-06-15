package br.facens.poo2.ac2project.dto.request.insert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotNull(message = "Ticket Attendee ID may not be blank.")
  @Positive(message = "Ticket Attendee ID must be positive.")
  private Long attendeeId;

  @NotBlank(message = "Ticket type may not be blank.")
  @Pattern(regexp = "^(?:FREE|PAID)$", message = "Ticket type must be either 'FREE' OR 'PAID'.")
  private String type;

}
