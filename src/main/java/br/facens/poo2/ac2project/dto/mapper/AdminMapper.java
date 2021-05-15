package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.entity.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper { 
    // Requests

    Admin toModel (AdminInsertRequest adminInsertRequest);
    Admin toModel(AdminUpdateRequest adminUpdateRequest);

    // Responses

    AdminResponse toAdminResponse(Admin admin);
}