package br.facens.poo2.event.scheduler.exception.generic;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalDateTimeFormatException extends ResponseStatusException {

  public IllegalDateTimeFormatException(Throwable e) {
    super(HttpStatus.BAD_REQUEST, "Provided date-time format not valid. "
        + "Date format: 'dd/MM/yyyy', Time format: 'HH:mm'." + e.getLocalizedMessage());
  }

}
