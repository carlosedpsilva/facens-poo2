package br.facens.poo2.event.scheduler.dto.request.update;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRequest {

  @Email(message = "Must be a valid e-mail.")
  private String email;

  @Pattern(
      regexp = "^(\\+?\\d{3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{5}[- .]?\\d{4}$",
      message = "Must be a valid phone number.")
  private String phoneNumber;

}
