package br.facens.poo2.ac2project.dto.request;

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

  @Pattern(
      regexp = "^(\\+?\\d{3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{5}[- .]?\\d{4}$",
      message = "Must be a valid phone number")
  private String phoneNumber;

}
