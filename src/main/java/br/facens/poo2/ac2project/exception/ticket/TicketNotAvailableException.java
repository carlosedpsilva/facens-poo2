package br.facens.poo2.ac2project.exception.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TicketNotAvailableException extends ResponseStatusException {

  public TicketNotAvailableException(boolean isPaidTicket) {
    super(HttpStatus.PRECONDITION_FAILED, "There are no " + (isPaidTicket ? "PAID" : "FREE")
        + " tickets available for this Event");
  }

}
