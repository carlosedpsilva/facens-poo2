package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlaceNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public PlaceNotFoundException(Long id) {
    super("Place not found with id " + id);
  }

  public HttpStatus status() {
    return HttpStatus.NOT_FOUND;
  }

}
