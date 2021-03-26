package br.facens.poo2.ac1project.exception;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
    return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    HashMap<String, ArrayList<ApiError>> apiErrors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String field = ((FieldError) error).getField();
      String code = ((FieldError) error).getCode();

      ApiError apiError = new ApiError(
          status,
          error.getDefaultMessage(),
          "Invalid field '" + field + "': " + code);

      if (!apiErrors.containsKey(field))
        apiErrors.put(field, new ArrayList<ApiError>());

      apiErrors.get(field).add(apiError);
    });

    return new ResponseEntity<Object>(apiErrors, headers, status);
  }

  @ExceptionHandler(IllegalEventDateTimeFormat.class)
  public ResponseEntity<Object> processValidationError(IllegalEventDateTimeFormat ex) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Event date-time format not valid. Date format: 'dd/MM/yyyy', Time format: 'HH:mm'.");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(IllegalDateScheduleException.class)
  public ResponseEntity<Object> processValidationError(IllegalDateScheduleException ex) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "The specified event date-time schedule is invalid (Start date-time must be before end date-time).");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(EventAlreadyRegisteredException.class)
  public ResponseEntity<Object> processValidationError(EventAlreadyRegisteredException ex) {
    ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), "The specified date-time is already reserved for the specified event.");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }

  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<Object> processValidationError(EventNotFoundException ex) {
    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "The specified event does not exist.");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus()); 
  }
}

@Getter
@AllArgsConstructor
final class ApiError {

  private HttpStatus status;
  private String message;
  private String error;
}