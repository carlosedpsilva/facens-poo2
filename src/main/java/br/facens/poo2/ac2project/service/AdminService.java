package br.facens.poo2.ac2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.AdminMapper;
import br.facens.poo2.ac2project.dto.request.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Admin;
import br.facens.poo2.ac2project.exception.AdminNotFoundException;
import br.facens.poo2.ac2project.repository.AdminRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {

  private static final String SAVED_MESSAGE = "Saved Admin with ID ";
  private static final String DELETED_MESSAGE = "Deleted Admin with ID ";
  private static final String UPDATED_MESSAGE = "Updated Admin with ID ";

  private AdminRepository adminRepository;

  private AdminMapper adminMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(AdminInsertRequest adminInsertRequest) {
    var adminToSave = adminMapper.toModel(adminInsertRequest);
    var savedAdmin = adminRepository.save(adminToSave);
    return createMessageResponse(savedAdmin.getId(), SAVED_MESSAGE);
  }

  /*
   * GET OPERATIONS
   */

  public Page<AdminResponse> findAll(Pageable pageRequest,
      String name, String email, String phoneNumber) {
    var entitySearchFilter = Admin.builder()
        .name(name.isBlank() ? null : name)
        .email(email.isBlank() ? null : email)
        .phoneNumber(phoneNumber.isBlank() ? null : phoneNumber)
        .build();

    Page<Admin> pagedAdmins = adminRepository.pageAll(pageRequest, entitySearchFilter);
    return pagedAdmins.map(adminMapper::toAdminResponse);
  }

  public AdminResponse findById(Long id) throws AdminNotFoundException {
    Admin savedAdmin = verifyIfExists(id);
    return adminMapper.toAdminResponse(savedAdmin);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(Long id) throws AdminNotFoundException {
    verifyIfExists(id);
    adminRepository.deleteById(id);
    return createMessageResponse(id, DELETED_MESSAGE);
  }

  /*
   * PUT OPERATION
   */

  public MessageResponse updateById(Long id, AdminUpdateRequest adminUpdateRequest)
      throws AdminNotFoundException {
    var adminToUpdate = verifyIfExists(id);

    adminToUpdate.setPhoneNumber(adminUpdateRequest.getPhoneNumber().isEmpty()
        ? adminToUpdate.getPhoneNumber()
        : adminUpdateRequest.getPhoneNumber());

    adminRepository.save(adminToUpdate);
    return createMessageResponse(adminToUpdate.getId(), UPDATED_MESSAGE);
  }

  /*
   * METHODS
   */

  private Admin verifyIfExists(Long id) throws AdminNotFoundException {
    return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
  }

  private MessageResponse createMessageResponse(Long id, String message) {
  return MessageResponse.builder()
      .message(message + id)
      .build();
  }

}
