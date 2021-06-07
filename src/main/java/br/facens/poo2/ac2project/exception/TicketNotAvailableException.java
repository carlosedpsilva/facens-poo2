package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class TicketNotAvailableException extends ProcessValidationException {

  public TicketNotAvailableException(boolean isPaidTicket) {
    super("There are no " + (isPaidTicket ? "PAID" : "FREE") + " tickets available.");
  }

  public HttpStatus status() {
    return HttpStatus.OK;
  }

}
