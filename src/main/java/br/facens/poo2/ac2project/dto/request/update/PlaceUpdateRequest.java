package br.facens.poo2.ac2project.dto.request.update;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceUpdateRequest {

  @Size(min = 2, max = 200, message = "Place name must be between 2 and 200 characters.")
  private String name;

}
