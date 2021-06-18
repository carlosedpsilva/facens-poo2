package br.facens.poo2.event.scheduler.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventNotFoundException extends ResponseStatusException {

  public EventNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, String.format("No Event found with ID %d.", id));
  }

}
