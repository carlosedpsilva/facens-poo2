package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalDateTimeFormatException extends ProcessValidationException {

  private static final long serialVersionUID = 1L;

  public IllegalDateTimeFormatException(Throwable e) {
    super("Event date-time format not valid. Date format: 'dd/MM/yyyy', Time format: 'HH:mm'." + e.getLocalizedMessage());
  }

  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }

}
