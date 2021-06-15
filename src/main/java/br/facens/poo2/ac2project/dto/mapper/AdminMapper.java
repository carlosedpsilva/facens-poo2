package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.insert.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.entity.Admin;

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
