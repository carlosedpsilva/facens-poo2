package br.facens.poo2.ac2project.util;

import br.facens.poo2.ac2project.dto.response.MessageResponse;

public class SchedulerUtils {

  public static final String BASIC_MESSAGE = "%s %s with ID %d";

  public static final String ASSOCIATION_MESSAGE = BASIC_MESSAGE + " with %s with ID %d";

  public static final String DOUBLE_ASSOCIATION_MESSAGE = BASIC_MESSAGE + " with %s with ID %d and %s with ID %d";

  public static final String ASSOCIATED_OPEATION_MESSAGE = BASIC_MESSAGE + " %s with %s with ID %d";

  public static final String DOUBLE_ASSOCIATED_OPERATION_MESSAGE = BASIC_MESSAGE + " %s with %s with ID %d and %s with ID %d";

  private SchedulerUtils() { }

  public static MessageResponse createMessageResponse(String message, Object... args) {
    return MessageResponse.builder().message(String.format(message, args)).build();
  }

  public enum Entity {
    ADMIN, ATTENDEE, EVENT, PLACE, TICKET;

    @Override
    public String toString() {
      return toString(false);
    }

    public String toString(boolean lower) {
      return lower ? name().toLowerCase() : name().charAt(0) + name().substring(1).toLowerCase();
    }
  }

  public enum Operation {
    ASSOCIATED, DEASSOCIATED, DELETED, SAVED, UPDATED;

    @Override
    public String toString() {
      return toString(false);
    }

    public String toString(boolean lower) {
      return lower ? name().toLowerCase() : name().charAt(0) + name().substring(1).toLowerCase();
    }
  }

}
