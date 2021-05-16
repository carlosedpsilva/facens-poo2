package br.facens.poo2.ac2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.AttendeeMapper;
import br.facens.poo2.ac2project.dto.request.AttendeeInsertRequest;
import br.facens.poo2.ac2project.dto.request.AttendeeUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AttendeeResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Attendee;
import br.facens.poo2.ac2project.exception.AttendeeNotFoundException;
import br.facens.poo2.ac2project.repository.AttendeeRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AttendeeService {

  private static final String SAVED_MESSAGE = "Saved Attendee with ID ";
  private static final String DELETED_MESSAGE = "Deleted Attendee with ID ";
  private static final String UPDATED_MESSAGE = "Updated Attendee with ID ";

  private AttendeeRepository attendeeRepository;
  private AttendeeMapper attendeeMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(AttendeeInsertRequest attendeeInsertRequest) {
    var attendeeToSave = attendeeMapper.toModel(attendeeInsertRequest);
    var savedAttendee = attendeeRepository.save(attendeeToSave);
    return createMessageResponse(savedAttendee.getId(), SAVED_MESSAGE);
  }

  /*
   * GET OPERATIONS
   */

  public Page<AttendeeResponse> findAll(Pageable pageRequest,
      String name, String email, String balance) {
    var entitySearchFilter = Attendee.builder()
        .name( name.isBlank() ? null : name )
        .email( email.isBlank() ? null : email )
        .balance( balance.isBlank() ? 0 : Double.valueOf(balance) )
        .build();

    Page<Attendee> pagedEvents = attendeeRepository.pageAll(pageRequest, entitySearchFilter);
    return pagedEvents.map(attendeeMapper::toAttendeeFindResponse);
  }

  public AttendeeResponse findById(Long id) throws AttendeeNotFoundException {
    Attendee savedAttendee = verifyIfExists(id);
    return attendeeMapper.toAttendeeFindResponse(savedAttendee);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(long id) throws AttendeeNotFoundException {
    verifyIfExists(id);
    attendeeRepository.deleteById(id);
    return createMessageResponse(id, DELETED_MESSAGE);
  }

  /*
   * UPDATE OPERATION
   */

  public MessageResponse updateById(Long id, AttendeeUpdateRequest attendeeUpdateRequest) throws AttendeeNotFoundException {
    var attendeeToUpdate = verifyIfExists(id);
    attendeeToUpdate.setBalance(attendeeUpdateRequest.getBalance());
    attendeeRepository.save(attendeeToUpdate);
    return createMessageResponse(attendeeToUpdate.getId(), UPDATED_MESSAGE);
  }

  /*
   * METHODS
   */

  private Attendee verifyIfExists(long id) throws AttendeeNotFoundException {
    return attendeeRepository.findById(id).orElseThrow(() -> new AttendeeNotFoundException(id));
  }

  private MessageResponse createMessageResponse(long id, String message) {
    return MessageResponse.builder()
        .message(message + id)
        .build();
  }

}
