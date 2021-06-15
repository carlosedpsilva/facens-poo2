package br.facens.poo2.ac2project.exception.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AdminNotFoundException extends ResponseStatusException {

  public AdminNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, String.format("No Admin found with ID %d.", id));
  }

}
