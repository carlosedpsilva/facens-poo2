package br.facens.poo2.event.scheduler.dto.request.insert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeInsertRequest {

  @NotBlank(message = "Attendee name may not be blank.")
  @Size(min = 2, max = 200, message = "User name must be between 2 and 200 characters.")
  private String name;

  @NotBlank(message = "Attendee e-mail may not be blank.")
  @Email(message = "Must be a valid e-mail.")
  private String email;

}
