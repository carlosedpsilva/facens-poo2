package br.facens.poo2.event.scheduler.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.facens.poo2.event.scheduler.exception.admin.AdminNotFoundException;
import br.facens.poo2.event.scheduler.exception.admin.EventAssociatedException;
import br.facens.poo2.event.scheduler.exception.attendee.AttendeeNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.EventNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.EventScheduleNotAvailableException;
import br.facens.poo2.event.scheduler.exception.event.IllegalScheduleException;
import br.facens.poo2.event.scheduler.exception.generic.EmailAlreadyInUseException;
import br.facens.poo2.event.scheduler.exception.generic.EmptyRequestException;
import br.facens.poo2.event.scheduler.exception.generic.IllegalDateTimeFormatException;
import br.facens.poo2.event.scheduler.exception.place.PlaceNotFoundException;
import br.facens.poo2.event.scheduler.exception.ticket.TicketNotAvailableException;
import br.facens.poo2.event.scheduler.exception.ticket.TicketNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String error = "No handler found for " + e.getHttpMethod() + " " + e.getRequestURL();
    var apiError = new ApiError(status.value(), status, error, e.getClass().getSimpleName());
    return new ResponseEntity<>(apiError, headers, apiError.getStatus());
  }

  @ExceptionHandler(value = {
    /* Generic  */ EmptyRequestException.class, EmailAlreadyInUseException.class, IllegalDateTimeFormatException.class,
    /* Admin    */ AdminNotFoundException.class, EventAssociatedException.class,
    /* Attendee */ AttendeeNotFoundException.class,
    /* Event    */ EventNotFoundException.class, EventScheduleNotAvailableException.class, IllegalScheduleException.class,
    /* Place    */ PlaceNotFoundException.class,
    /* Ticket   */ TicketNotFoundException.class, TicketNotAvailableException.class,
  })
  public ResponseEntity<Object> handleProcessValidation(ResponseStatusException e) {
    var apiError = new ApiError(e.getStatus().value(), e.getStatus(), e.getReason(), e.getClass().getSimpleName());
    return new ResponseEntity<>(apiError, new HttpHeaders(), e.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    var apiError = new ApiError(BAD_REQUEST.value(), BAD_REQUEST, ex.getCause().getLocalizedMessage(), ex.getClass().getSimpleName());
    return new ResponseEntity<>(apiError, new HttpHeaders(), BAD_REQUEST);
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
