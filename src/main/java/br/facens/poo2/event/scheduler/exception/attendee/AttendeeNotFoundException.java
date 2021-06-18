package br.facens.poo2.event.scheduler.exception.attendee;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AttendeeNotFoundException extends ResponseStatusException {

  public AttendeeNotFoundException(long id) {
    this(id, false);
  }

  public AttendeeNotFoundException(long id, boolean isAssociation) {
    super(HttpStatus.NOT_FOUND, String.format("No Attendee found %swith ID %d.", (isAssociation ? "associated Ticket " : ""), id));
  }

}
