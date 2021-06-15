package br.facens.poo2.ac2project.utils;

import br.facens.poo2.ac2project.dto.request.insert.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.entity.Admin;

public class AdminUtils {

  private static Long   ID           = 1L;
  private static String NAME         = "Fake Name";
  private static String EMAIL        = "example@email.com";
  private static String PHONE_NUMBER = "(99) 99999-9999";

  public static Admin createFakeEntity() {
    return Admin.builder()
        .id(ID)
        .name(NAME)
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .build();
  }

  /*
   * Request
   */

  public static AdminInsertRequest createFakeInsertRequest() {
    return AdminInsertRequest.builder()
        .name(NAME)
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .build();
  }

  public static AdminUpdateRequest createFakeUpdateRequest() {
    return AdminUpdateRequest.builder()
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .build();
  }

  /*
   * Response
   */

  public static AdminResponse createFakeResponse() {
    return AdminResponse.builder()
        .id(ID)
        .name(NAME)
        .email(EMAIL)
        .phoneNumber(PHONE_NUMBER)
        .build();
  }

}
