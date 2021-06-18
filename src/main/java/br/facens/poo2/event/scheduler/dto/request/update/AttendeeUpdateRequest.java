package br.facens.poo2.event.scheduler.dto.request.update;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeUpdateRequest {

  @Email(message = "Must be a valid e-mail.")
  private String email;

}
