package br.facens.poo2.ac1project.exception;

public class IllegalEventDateTimeFormat extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalEventDateTimeFormat(Throwable e) {
    super(e.getLocalizedMessage());
  }
  
}
