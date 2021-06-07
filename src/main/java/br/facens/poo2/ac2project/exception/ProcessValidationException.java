package br.facens.poo2.ac2project.exception;

import org.springframework.http.HttpStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class ProcessValidationException extends RuntimeException {

  protected ProcessValidationException(String message) {
    super(message);
  }

  public abstract HttpStatus status();
}
