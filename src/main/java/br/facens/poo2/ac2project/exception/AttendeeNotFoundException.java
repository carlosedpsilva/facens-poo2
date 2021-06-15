package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttendeeNotFoundException extends ProcessValidationException {

  public AttendeeNotFoundException(long id) {
    this(id, false);
  }

  public AttendeeNotFoundException(long id, boolean isAssociation) {
    super("No Attendee found " + (isAssociation ? "associated Ticket" : "") + " with ID " + id);
  }

  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
