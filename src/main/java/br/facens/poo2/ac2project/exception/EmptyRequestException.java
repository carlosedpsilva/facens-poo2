package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyRequestException extends Exception {

  private static final long serialVersionUID = 1L;

  public EmptyRequestException() {
    super("Cannot update an event with no new values.");
  }

  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }
  
}
