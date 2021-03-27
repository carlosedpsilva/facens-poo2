package br.facens.poo2.ac1project.exception;

import java.time.format.DateTimeFormatter;

import br.facens.poo2.ac1project.entity.Event;

public class EventAlreadyRegisteredException extends Exception {

  private static final long serialVersionUID = -6052871050427397282L;

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public EventAlreadyRegisteredException(Event e) {
    super("Could not register event. There's an already scheduled event with name '" + e.getName()
        + "' and place '" + e.getPlace() + "' which conflicted with the specified date-time. Event scheduled"
        + " from " + e.getStartDate().format(dtf) + " " + e.getStartTime()
        +  " to " +   e.getEndDate().format(dtf) + " " + e.getEndTime());
  }

}
