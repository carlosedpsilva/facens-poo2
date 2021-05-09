package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public EventNotFoundException(Long id) {
    super("Event not found with id " + id);
  }

  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
