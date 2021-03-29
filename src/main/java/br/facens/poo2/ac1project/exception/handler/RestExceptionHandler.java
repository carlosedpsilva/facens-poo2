package br.facens.poo2.ac1project.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

import br.facens.poo2.ac1project.exception.EventNotFoundException;
import br.facens.poo2.ac1project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac1project.exception.IllegalScheduleException;
import br.facens.poo2.ac1project.exception.IllegalDateTimeFormatException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error);
    return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    HashMap<String, ArrayList<RequestFieldError>> fieldErrors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String field = ((FieldError) error).getField();
      String code = ((FieldError) error).getCode();

      RequestFieldError fieldError = new RequestFieldError(
          error.getDefaultMessage(),
          "Constraint: " + code);

      if (!fieldErrors.containsKey(field))
        fieldErrors.put(field, new ArrayList<RequestFieldError>());

      fieldErrors.get(field).add(fieldError);
    });

    InvalidRequestError wow = new InvalidRequestError(
        status,
        "Missing required fields or wrong field range value.",
        fieldErrors);

    return new ResponseEntity<Object>(wow, headers, status);
  }

  @ExceptionHandler(IllegalDateTimeFormatException.class)
  public ResponseEntity<Object> processValidationError(IllegalDateTimeFormatException ex) {
    ApiError apiError = new ApiError(ex.status(), ex.getLocalizedMessage(), "Event date-time format not valid. Date format: 'dd/MM/yyyy', Time format: 'HH:mm'");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(IllegalScheduleException.class)
  public ResponseEntity<Object> processValidationError(IllegalScheduleException ex) {
    ApiError apiError = new ApiError(ex.status(), ex.getLocalizedMessage(), "The specified event date-time schedule is invalid (Start date-time must be before end date-time)");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(EventScheduleNotAvailableException.class)
  public ResponseEntity<Object> processValidationError(EventScheduleNotAvailableException ex) {
    ApiError apiError = new ApiError(ex.status(), ex.getLocalizedMessage(), "The specified date-time conflicted with an already scheduled date-time for the specified Event name and place");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<Object> processValidationError(EventNotFoundException ex) {
    ApiError apiError = new ApiError(ex.status(), ex.getLocalizedMessage(), "The specified event does not exist.");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }
}

@Getter
@AllArgsConstructor
final class ApiError {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ApiError(HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.errors = Arrays.asList(error);
  }
}

@Getter
@AllArgsConstructor
final class InvalidRequestError {

  private HttpStatus status;
  private String message;
  private HashMap<String, ArrayList<RequestFieldError>> errors;

}
@Getter
@AllArgsConstructor
final class RequestFieldError {

  private String message;
  private String error;

}