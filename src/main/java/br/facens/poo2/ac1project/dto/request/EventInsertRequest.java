package br.facens.poo2.ac1project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventInsertRequest {
  
  @NotEmpty(message = "Event name cannot be empty")
  @Size(min = 2, max = 200, message = "Event name must be between 2 and 200 characters")
  private String name;
  
  @Size(min = 0, max = 500, message = "Event description must contain less than 500 characters")
  private String description;
  
  @NotEmpty(message = "Event place cannot be empty")
  @Size(min = 2, max = 100, message = "Event place must be between 2 and 100 characters")
  private String place;
  
  @NotEmpty(message = "Event start date cannot be empty")
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private String startDate;
  
  @NotEmpty(message = "Event end date cannot be empty")
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private String endDate;
  
  @NotEmpty(message = "Event start time cannot be empty")
  @DateTimeFormat(pattern = "HH:mm")
  private String startTime;
  
  @NotEmpty(message = "Event end time cannot be empty")
  @DateTimeFormat(pattern = "HH:mm")
  private String endTime;
  
  @Email(message = "Email must be valid")
  private String email;

}
