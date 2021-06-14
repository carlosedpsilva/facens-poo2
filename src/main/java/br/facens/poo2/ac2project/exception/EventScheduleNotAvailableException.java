package br.facens.poo2.ac2project.exception;

import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Place;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventScheduleNotAvailableException extends ProcessValidationException {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public EventScheduleNotAvailableException(Event e, Place p) {
    super("Could not register event. There is a already scheduled event"
        + " with name '" + e.getName() + "' ID " + e.getId()
        + " and place '" + p.getName() + "' ID " + p.getId()
        + " which conflicted with the specified date-time. Event scheduled"
        + " from " + e.getStartDate().format(dtf) + " " + e.getStartTime()
        +   " to " +   e.getEndDate().format(dtf) + " " + e.getEndTime());
  }

  public HttpStatus status() {
    return HttpStatus.CONFLICT;
  }

}
