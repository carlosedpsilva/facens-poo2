package br.facens.poo2.ac2project.exception.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventAssociatedException extends ResponseStatusException {

  public EventAssociatedException(long adminId) {
    super(HttpStatus.PRECONDITION_FAILED, String.format("Could not delete Admin with ID %d. "
        + "This Admin is associated with one or more Events.", adminId));
  }

}
