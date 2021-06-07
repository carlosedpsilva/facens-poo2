package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.TicketInsertRequest;
import br.facens.poo2.ac2project.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

  /*
   * Requests
   */

  Ticket toModel(TicketInsertRequest ticketInsertRequest);

}
