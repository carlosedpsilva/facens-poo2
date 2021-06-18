package br.facens.poo2.event.scheduler.dto.request.update;

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

  /*
   * Event schedule may not be updated, user must delete existing Event and/or
   * create another one
   */

  @Size(min = 2, max = 200, message = "Event name may be between 2 and 200 characters.")
  private String name;

  @Size(min = 0, max = 500, message = "Event description must contain less than 500 characters.")
  private String description;

  @Email(message = "Event e-mail must be valid.")
  private String email;

}
