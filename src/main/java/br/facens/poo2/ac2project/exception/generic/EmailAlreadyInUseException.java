package br.facens.poo2.ac2project.exception.generic;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyInUseException extends ResponseStatusException {

  public EmailAlreadyInUseException() {
    super(HttpStatus.CONFLICT, "The email address is already in use by another user.");
  }

}
