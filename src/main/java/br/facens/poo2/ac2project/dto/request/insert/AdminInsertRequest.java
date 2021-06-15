package br.facens.poo2.ac2project.dto.request.insert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminInsertRequest {

  @NotBlank(message = "Admin name may not be blank.")
  @Size(min = 2, max = 200, message = "User name must be between 2 and 200 characters.")
  private String name;

  @NotBlank(message = "Admin e-mail may not be blank.")
  @Email(message = "Must be a valid e-mail.")
  private String email;

  @NotBlank(message = "Admin phone number may not be blank.")
  @Pattern(
      regexp = "^(\\+?\\d{3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{5}[- .]?\\d{4}$",
      message = "Must be a valid phone number.")
  private String phoneNumber;

}
