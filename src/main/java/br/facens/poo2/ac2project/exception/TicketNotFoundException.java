package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNotFoundException extends ProcessValidationException {

  public TicketNotFoundException(long id) {
    super("Ticket not found with ID " + id);
  }

  @Override
  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
