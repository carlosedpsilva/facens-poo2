package br.facens.poo2.ac1project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalDateTimeFormatException extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalDateTimeFormatException(Throwable e) {
    super(e.getLocalizedMessage());
  }
  
  public HttpStatus status() {
    return HttpStatus.BAD_REQUEST;
  }

}
