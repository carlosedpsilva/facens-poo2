package br.facens.poo2.ac2project.exception.generic;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyRequestException extends ResponseStatusException {

  public EmptyRequestException() {
    super(HttpStatus.BAD_REQUEST, "This request requires a not blank body.");
  }

}
