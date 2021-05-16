package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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

  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 2, max = 200, message = "User name must be between 2 and 200 characters")
  private String name;

  @Email(message = "Must be a valid e-mail")
  private String email;

  @Size(min = 11, max = 11, message = "Must be a valid phone number")
  private String phoneNumber;

}
