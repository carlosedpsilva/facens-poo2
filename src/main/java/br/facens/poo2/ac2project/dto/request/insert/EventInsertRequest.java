package br.facens.poo2.ac2project.dto.request.insert;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotNull(message = "Event Admin ID may not be blank.")
  @Positive(message = "Event Admin ID must be positive.")
  private Long adminId;

  @NotBlank(message = "Event name may not be blank.")
  @Size(min = 2, max = 200, message = "Event name may be between 2 and 200 characters.")
  private String name;

  @Size(min = 0, max = 500, message = "Event description must contain less than 500 characters.")
  private String description;

  @NotBlank(message = "Event start date may not be blank.")
  private String startDate;

  @NotBlank(message = "Event end date may not be blank.")
  private String endDate;

  @NotBlank(message = "Event start time may not be blank.")
  private String startTime;

  @NotBlank(message = "Event end time may not be blank.")
  private String endTime;

  @NotBlank(message = "Event e-mail may not be blank.")
  @Email(message = "Event e-mail must be valid.")
  private String email;

  @NotNull(message = "Event amount of free tickets may not be blank.")
  @PositiveOrZero(message = "Amount of free tickets may be positive or zero.")
  private Long amountFreeTickets;

  @NotNull(message = "Event amount of paid tickets may not be blank.")
  @PositiveOrZero(message = "Amount of paid tickets may be positive or zero.")
  private Long amountPaidTickets;

  @NotNull(message = "Event paid tickets price may not be blank.")
  @PositiveOrZero(message = "Ticket price may be positive or zero.")
  @Digits(integer = 12, fraction = 2, message = "Ticket price must follow the following format: '0.00'.")
  private Double ticketPrice;

}
