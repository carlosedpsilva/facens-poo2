package br.facens.poo2.event.scheduler.exception.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalScheduleException extends ResponseStatusException {

  public IllegalScheduleException(String message) {
    super(HttpStatus.BAD_REQUEST, "The specified Event date-time schedule is invalid. " + message);
  }

}
