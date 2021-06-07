package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
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
public class EventInsertRequest {

  @Positive(message = "Invalid admin Id.")
  private Long adminId;

  @NotEmpty(message = "Event name cannot be empty")
  @Size(min = 2, max = 200, message = "Event name must be between 2 and 200 characters")
  private String name;

  @Size(min = 0, max = 500, message = "Event description must contain less than 500 characters")
  private String description;

  @NotEmpty(message = "Event start date cannot be empty")
  private String startDate;

  @NotEmpty(message = "Event end date cannot be empty")
  private String endDate;

  @NotEmpty(message = "Event start time cannot be empty")
  private String startTime;

  @NotEmpty(message = "Event end time cannot be empty")
  private String endTime;

  @Email(message = "Email must be valid")
  private String emailContact;

  @PositiveOrZero(message = "Amount of free tickets must be positive or zero")
  private Long amountFreeTickets;

  @PositiveOrZero(message = "Amount of paid tickets must be positive or zero")
  private Long amountPaidTickets;

  @PositiveOrZero(message = "Ticket price must be positive or zero")
  private Double priceTicket;

}
