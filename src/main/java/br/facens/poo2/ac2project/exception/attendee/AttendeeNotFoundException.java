package br.facens.poo2.ac2project.exception.attendee;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AttendeeNotFoundException extends ResponseStatusException {

  public AttendeeNotFoundException(long id) {
    this(id, false);
  }

  public AttendeeNotFoundException(long id, boolean isAssociation) {
    super(HttpStatus.NOT_FOUND, String.format("No Attendee found %s with ID %d.", (isAssociation ? "associated Ticket" : ""), id));
  }

}
