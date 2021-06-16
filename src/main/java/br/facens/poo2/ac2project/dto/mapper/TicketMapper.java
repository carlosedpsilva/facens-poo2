package br.facens.poo2.ac2project.dto.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.facens.poo2.ac2project.dto.info.TicketComponentInfo;
import br.facens.poo2.ac2project.dto.info.TicketPageComponentInfo;
import br.facens.poo2.ac2project.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.ac2project.dto.response.TicketPageComponentResponse;
import br.facens.poo2.ac2project.dto.response.TicketResponse;
import br.facens.poo2.ac2project.entity.Ticket;

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
