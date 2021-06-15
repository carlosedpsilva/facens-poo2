package br.facens.poo2.ac2project.exception.handler;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.facens.poo2.ac2project.exception.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.AttendeeNotFoundException;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.EventNotFoundException;
import br.facens.poo2.ac2project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.IllegalScheduleException;
import br.facens.poo2.ac2project.exception.PlaceNotFoundException;
import br.facens.poo2.ac2project.exception.ProcessValidationException;
import br.facens.poo2.ac2project.exception.TicketNotAvailableException;
import br.facens.poo2.ac2project.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String error = "No handler found for " + e.getHttpMethod() + " " + e.getRequestURL();
    var apiError = new ApiError(status.value(), status, error, e.getClass().getSimpleName());
    return new ResponseEntity<>(apiError, headers, apiError.getStatus());
  }

  @ExceptionHandler(value = {
    /* Generic  */ EmptyRequestException.class, IllegalDateTimeFormatException.class,
    /* Admin    */ AdminNotFoundException.class,
    /* Attendee */ AttendeeNotFoundException.class,
    /* Event    */ EventNotFoundException.class, EventScheduleNotAvailableException.class, IllegalScheduleException.class,
    /* Place    */ PlaceNotFoundException.class,
    /* Ticket   */ TicketNotFoundException.class, TicketNotAvailableException.class,
  })
  public ResponseEntity<Object> processValidationError(ProcessValidationException e) {
    var apiError = new ApiError(e.status().value(), e.status(), e.getLocalizedMessage(), e.getClass().getSimpleName());
    return new ResponseEntity<>(apiError, new HttpHeaders(), e.status());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    HashMap<String, ArrayList<RequestFieldError>> fieldErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String field = ((FieldError) error).getField();
      String code = ((FieldError) error).getCode();

      var fieldError = new RequestFieldError(error.getDefaultMessage(), code);
      if (!fieldErrors.containsKey(field)) fieldErrors.put(field, new ArrayList<>());

      fieldErrors.get(field).add(fieldError);
    });

    var invalidRequestBodyError = new InvalidRequestBodyError(status.value(), status,
        "Missing required fields or wrong field range value.", fieldErrors);
    return new ResponseEntity<>(invalidRequestBodyError, headers, status);
  }

}

@Getter
@AllArgsConstructor
final class ApiError {

  private int code;
  private HttpStatus status;
  private String message;
  private String error;

}

@Getter
@AllArgsConstructor
final class InvalidRequestBodyError {

  private int code;
  private HttpStatus status;
  private String message;
  private HashMap<String, ArrayList<RequestFieldError>> errors;

}
@Getter
@AllArgsConstructor
final class RequestFieldError {

  private String message;
  private String constraint;

}
