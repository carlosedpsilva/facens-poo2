package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
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

  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 2, max = 200, message = "User name must be between 2 and 200 characters")
  private String name;

  @Email(message = "Must be a valid e-mail")
  private String email;

  @PositiveOrZero(message = "Balance cannot be negative")
  private double balance;

}
