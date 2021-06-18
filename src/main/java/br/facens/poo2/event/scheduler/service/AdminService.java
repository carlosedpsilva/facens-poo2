package br.facens.poo2.event.scheduler.service;

import static br.facens.poo2.event.scheduler.util.SchedulerUtils.BASIC_MESSAGE;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Entity.ADMIN;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.SAVED;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.UPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.event.scheduler.dto.mapper.AdminMapper;
import br.facens.poo2.event.scheduler.dto.request.insert.AdminInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.AdminUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.AdminResponse;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import br.facens.poo2.event.scheduler.entity.Admin;
import br.facens.poo2.event.scheduler.exception.admin.AdminNotFoundException;
import br.facens.poo2.event.scheduler.exception.admin.EventAssociatedException;
import br.facens.poo2.event.scheduler.exception.generic.EmailAlreadyInUseException;
import br.facens.poo2.event.scheduler.exception.generic.EmptyRequestException;
import br.facens.poo2.event.scheduler.repository.AdminRepository;
import br.facens.poo2.event.scheduler.repository.EventRepository;
import br.facens.poo2.event.scheduler.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService implements SchedulerService<Admin> {

  private final AdminRepository adminRepository;
  private final EventRepository eventRepository;

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
    if (!eventRepository.findByAdminId(id).isEmpty()) throw new EventAssociatedException(id);
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
