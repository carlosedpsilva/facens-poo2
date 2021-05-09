package br.facens.poo2.ac2project.exception;

import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.facens.poo2.ac2project.entity.Event;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventScheduleNotAvailableException extends Exception {

  private static final long serialVersionUID = -6052871050427397282L;

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public EventScheduleNotAvailableException(Event e) {
    super("Could not register event. There's an already scheduled event with name '" + e.getName()
        + "' and place '" + e.getPlace() + "' which conflicted with the specified date-time. Event scheduled"
        + " from " + e.getStartDate().format(dtf) + " " + e.getStartTime()
        +  " to " +   e.getEndDate().format(dtf) + " " + e.getEndTime());
  }

  public HttpStatus status() {
    return HttpStatus.CONFLICT;
  }

}
