package br.facens.poo2.ac1project.exception;

public class EventNotFoundException extends Exception {
  
  private static final long serialVersionUID = 1L;

  public EventNotFoundException(Long id) {
    super("Event not found with id " + id);
  }
}
