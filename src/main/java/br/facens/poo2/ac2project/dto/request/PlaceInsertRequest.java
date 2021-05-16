package br.facens.poo2.ac2project.dto.request;

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
public class PlaceInsertRequest {

  @NotEmpty(message = "Name cannot be empty")
  @Size(min = 2, max = 200, message = "Place name must be between 2 and 200 characters")
  private String name;

  @NotEmpty(message = "Address cannot be empty")
  @Size(min = 2, max = 100, message = "Place address must be between 5 and 200 characters")
  private String address;

}
