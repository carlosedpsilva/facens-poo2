package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.util.SchedulerUtils.BASIC_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.ATTENDEE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.SAVED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.UPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.AttendeeMapper;
import br.facens.poo2.ac2project.dto.request.insert.AttendeeInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.AttendeeUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AttendeeResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Attendee;
import br.facens.poo2.ac2project.exception.attendee.AttendeeNotFoundException;
import br.facens.poo2.ac2project.exception.generic.EmailAlreadyInUseException;
import br.facens.poo2.ac2project.exception.generic.EmptyRequestException;
import br.facens.poo2.ac2project.repository.AttendeeRepository;
import br.facens.poo2.ac2project.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendeeService implements SchedulerService<Attendee> {

  private final AttendeeRepository attendeeRepository;

  private final AttendeeMapper attendeeMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(AttendeeInsertRequest attendeeInsertRequest) {
    if (attendeeRepository.findByEmail(attendeeInsertRequest.getEmail()).isPresent()) throw new EmailAlreadyInUseException();
    var attendeeToSave = attendeeMapper.toModel(attendeeInsertRequest);
    var savedAttendee = attendeeRepository.save(attendeeToSave);
    return createMessageResponse(BASIC_MESSAGE, SAVED, ATTENDEE, savedAttendee.getId());
  }

  /*
   * GET OPERATION
   */

  public Page<AttendeeResponse> findAll(Pageable pageRequest,
      String name, String email, Double balance) {
    var entitySearchFilter = Attendee.builder()
        .name(name.isBlank() ? null : name)
        .email(email.isBlank() ? null : email)
        .balance(balance == null ? 0 : balance)
        .build();

    Page<Attendee> pagedEvents = attendeeRepository.pageAll(pageRequest, entitySearchFilter);
    return pagedEvents.map(attendeeMapper::toAttendeeFindResponse);
  }

  public AttendeeResponse findById(long id) throws AttendeeNotFoundException {
    Attendee savedAttendee = verifyIfExists(id);
    return attendeeMapper.toAttendeeFindResponse(savedAttendee);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(long id) throws AttendeeNotFoundException {
    verifyIfExists(id);
    attendeeRepository.deleteById(id);
    return createMessageResponse(BASIC_MESSAGE, DELETED, ATTENDEE, id);
  }

  /*
   * PUT OPERATION
   */

  public MessageResponse updateById(long id, AttendeeUpdateRequest attendeeUpdateRequest) throws AttendeeNotFoundException {
    var attendeeToUpdate = verifyIfExists(id);

    if (attendeeUpdateRequest.getEmail().isBlank()) throw new EmptyRequestException();
    attendeeToUpdate.setEmail(attendeeUpdateRequest.getEmail());

    attendeeRepository.save(attendeeToUpdate);
    return createMessageResponse(BASIC_MESSAGE, UPDATED, ATTENDEE, id);
  }

  /*
   * OTHER
   */

  @Override
  public Attendee verifyIfExists(long id) throws AttendeeNotFoundException {
    return attendeeRepository.findById(id).orElseThrow(() -> new AttendeeNotFoundException(id));
  }

}
