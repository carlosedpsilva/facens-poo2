package br.facens.poo2.event.scheduler.dto.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.facens.poo2.event.scheduler.dto.info.TicketComponentInfo;
import br.facens.poo2.event.scheduler.dto.info.TicketPageComponentInfo;
import br.facens.poo2.event.scheduler.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.event.scheduler.dto.response.TicketPageComponentResponse;
import br.facens.poo2.event.scheduler.dto.response.TicketResponse;
import br.facens.poo2.event.scheduler.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

  /*
   * Request
   */

  Ticket toModel(TicketInsertRequest ticketInsertRequest);

  /*
   * Response
   */

  @Mapping(target = "date", source = "date", dateFormat = "dd/MM/yyyy HH:mm:ss")
  TicketResponse toTicketResponse(TicketComponentInfo ticketComponentInfo, LocalDateTime date);

  TicketPageComponentResponse toTicketPageComponentResponse(TicketPageComponentInfo ticketPageComponentInfo);

}
