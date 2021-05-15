package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeUpdateRequest {
  
  @PositiveOrZero(message = "Balance must be positive or zero")
  private Double balance;

}
