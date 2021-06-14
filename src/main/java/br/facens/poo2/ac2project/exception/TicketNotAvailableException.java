package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class TicketNotAvailableException extends ProcessValidationException {

  public TicketNotAvailableException(boolean isPaidTicket) {
    super("There are no " + (isPaidTicket ? "PAID" : "FREE") + " tickets available.");
  }

  @Override
  public HttpStatus status() {
    return HttpStatus.PRECONDITION_FAILED;
  }

}
