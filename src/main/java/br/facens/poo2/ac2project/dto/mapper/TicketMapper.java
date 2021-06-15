package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.ac2project.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

  /*
   * Request
   */

  Ticket toModel(TicketInsertRequest ticketInsertRequest);

}
