package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalScheduleException extends ProcessValidationException {

  public IllegalScheduleException(String message) {
    super("The specified event date-time schedule is invalid. " + message);
  }

  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }

}
