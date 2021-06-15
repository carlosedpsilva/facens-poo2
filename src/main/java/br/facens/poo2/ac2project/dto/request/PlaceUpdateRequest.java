package br.facens.poo2.ac2project.dto.request;

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
public class PlaceUpdateRequest {

  @NotBlank(message = "New Place name may not be blank")
  @Size(min = 2, max = 200, message = "Place name must be between 2 and 200 characters")
  private String name;

}
