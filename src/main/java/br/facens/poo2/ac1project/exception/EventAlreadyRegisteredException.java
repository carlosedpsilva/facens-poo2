package br.facens.poo2.ac1project.exception;

public class EventAlreadyRegisteredException extends Exception {

  private static final long serialVersionUID = -6052871050427397282L;

  public EventAlreadyRegisteredException(String eventName) {
    super("Could not register event. An event with the name " + eventName + " is already registered for the specified date-time.");
  }

}
