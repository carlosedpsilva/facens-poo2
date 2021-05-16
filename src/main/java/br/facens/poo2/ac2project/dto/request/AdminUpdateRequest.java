package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRequest {

  @Size(min = 11, max = 11, message = "Must be a valid phone number")
  private String phoneNumber;

}
