package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyRequestException extends ProcessValidationException {

  public EmptyRequestException() {
    super("This request requires a not blank body");
  }

  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }

}
