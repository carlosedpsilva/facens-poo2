package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends ProcessValidationException {

  public EventNotFoundException(Long id) {
    super("Event not found with ID " + id);
  }

  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
