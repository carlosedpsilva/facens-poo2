package br.facens.poo2.event.scheduler.exception.place;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PlaceNotFoundException extends ResponseStatusException {

  private static final long serialVersionUID = 1L;

  public PlaceNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Place not found with ID " + id);
  }

}
