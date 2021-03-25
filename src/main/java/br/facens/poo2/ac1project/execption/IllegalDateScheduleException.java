package br.facens.poo2.ac1project.execption;

public class IllegalDateScheduleException extends Exception {
  
  private static final long serialVersionUID = 1L;

  public IllegalDateScheduleException() {
    super("Event start date time must be before the end date time");
  }
}
