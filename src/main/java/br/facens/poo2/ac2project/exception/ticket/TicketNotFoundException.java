package br.facens.poo2.ac2project.exception.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TicketNotFoundException extends ResponseStatusException {

  public TicketNotFoundException(long id) {
    super(HttpStatus.NOT_FOUND, "Ticket not found with ID " + id);
  }

}
