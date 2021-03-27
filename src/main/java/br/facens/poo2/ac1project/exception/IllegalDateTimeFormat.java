package br.facens.poo2.ac1project.exception;

public class IllegalDateTimeFormat extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalDateTimeFormat(Throwable e) {
    super(e.getLocalizedMessage());
  }
  
}
