package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequest {

  @Size(min = 0, max = 200, message = "Event name must be between 2 and 200 characters")
  private String name;

  @Size(min = 0, max = 500, message = "Event description must contain less than 500 characters")
  private String description;

  @Email(message = "Email must be valid")
  private String emailContact;

}
