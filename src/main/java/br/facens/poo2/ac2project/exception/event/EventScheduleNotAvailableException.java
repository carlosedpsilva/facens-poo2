package br.facens.poo2.ac2project.exception.event;

import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Place;

public class EventScheduleNotAvailableException extends ResponseStatusException {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public EventScheduleNotAvailableException(Event e, Place p) {
    super(HttpStatus.CONFLICT, String.format("Could not register event. There is a already scheduled event"
        + " with name '%s' with ID %d and place '%s' with ID %d which conflicted with the specified date-time. "
        + "Event scheduled from %s %s to %s %s.", e.getName(), e.getId(), p.getName(), p.getId(),
        e.getStartDate().format(dtf), e.getStartTime(), e.getEndDate().format(dtf), e.getEndTime()));
  }

}
