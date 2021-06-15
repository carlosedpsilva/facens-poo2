package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.util.SchedulerUtils.BASIC_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.ADMIN;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.SAVED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.UPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.AdminMapper;
import br.facens.poo2.ac2project.dto.request.insert.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Admin;
import br.facens.poo2.ac2project.exception.admin.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.generic.EmailAlreadyInUseException;
import br.facens.poo2.ac2project.exception.generic.EmptyRequestException;
import br.facens.poo2.ac2project.repository.AdminRepository;
import br.facens.poo2.ac2project.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService implements SchedulerService<Admin> {

  private final AdminRepository adminRepository;

  private final AdminMapper adminMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(AdminInsertRequest adminInsertRequest) {
    if (adminRepository.findByEmail(adminInsertRequest.getEmail()).isPresent()) throw new EmailAlreadyInUseException();
    var adminToSave = adminMapper.toModel(adminInsertRequest);
    var savedAdmin = adminRepository.save(adminToSave);
    return createMessageResponse(BASIC_MESSAGE, SAVED, ADMIN, savedAdmin.getId());
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

  public AdminResponse findById(long id) throws AdminNotFoundException {
    Admin savedAdmin = verifyIfExists(id);
    return adminMapper.toAdminResponse(savedAdmin);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(long id) throws AdminNotFoundException {
    verifyIfExists(id);
    adminRepository.deleteById(id);
    return createMessageResponse(BASIC_MESSAGE, DELETED, ADMIN, id);
  }

  /*
   * PUT OPERATION
   */

  public MessageResponse updateById(long id, AdminUpdateRequest adminUpdateRequest)
      throws AdminNotFoundException {
    var adminToUpdate = verifyIfExists(id);

    if (adminUpdateRequest.getEmail().isBlank()
        && adminUpdateRequest.getPhoneNumber().isBlank())
      throw new EmptyRequestException();

    adminToUpdate.setEmail(
        adminUpdateRequest.getEmail().isBlank()
        ? adminToUpdate.getEmail()
        : adminUpdateRequest.getEmail());

    adminToUpdate.setPhoneNumber(
        adminUpdateRequest.getPhoneNumber().isBlank()
        ? adminToUpdate.getPhoneNumber()
        : adminUpdateRequest.getPhoneNumber());

    adminRepository.save(adminToUpdate);
    return createMessageResponse(BASIC_MESSAGE, UPDATED, ADMIN, id);
  }

  /*
   * OTHER
   */

  @Override
  public Admin verifyIfExists(long id) throws AdminNotFoundException {
    return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
  }

}
