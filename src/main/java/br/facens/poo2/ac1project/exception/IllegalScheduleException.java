package br.facens.poo2.ac1project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalScheduleException extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalScheduleException(String message) {
    super(message);
  }

  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }

}
