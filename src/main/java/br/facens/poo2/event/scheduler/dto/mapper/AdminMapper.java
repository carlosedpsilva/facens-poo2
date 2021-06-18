package br.facens.poo2.event.scheduler.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.event.scheduler.dto.request.insert.AdminInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.AdminUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.AdminResponse;
import br.facens.poo2.event.scheduler.entity.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

  /*
   * Request
   */

  Admin toModel(AdminInsertRequest adminInsertRequest);

  Admin toModel(AdminUpdateRequest adminUpdateRequest);

  /*
   * Response
   */

  AdminResponse toAdminResponse(Admin admin);

}
