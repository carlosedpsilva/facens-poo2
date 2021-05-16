package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttendeeNotFoundException extends Exception {
  
  public AttendeeNotFoundException(long id) {
    super("Attendee not found with id " + id);
  }

  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
