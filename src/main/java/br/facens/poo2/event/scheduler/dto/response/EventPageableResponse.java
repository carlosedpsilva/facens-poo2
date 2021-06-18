package br.facens.poo2.event.scheduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPageableResponse {

  private Long eventId;
  private Long adminId;
  private String name;
  private String description;
  private String startDate;
  private String startTime;

}
