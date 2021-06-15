package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.util.SchedulerUtils.DOUBLE_ASSOCIATED_OPERATION_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.ATTENDEE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.EVENT;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.TICKET;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.ASSOCIATED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.SAVED;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.TicketMapper;
import br.facens.poo2.ac2project.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Ticket;
import br.facens.poo2.ac2project.enums.TicketType;
import br.facens.poo2.ac2project.exception.attendee.AttendeeNotFoundException;
import br.facens.poo2.ac2project.exception.event.EventNotFoundException;
import br.facens.poo2.ac2project.exception.ticket.TicketNotAvailableException;
import br.facens.poo2.ac2project.exception.ticket.TicketNotFoundException;
import br.facens.poo2.ac2project.repository.AttendeeRepository;
import br.facens.poo2.ac2project.repository.TicketRepository;
import br.facens.poo2.ac2project.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketService implements SchedulerService<Ticket> {

  private final EventService eventService;
  private final AttendeeService attendeeService;

  private final TicketRepository ticketRepository;
  private final AttendeeRepository attendeeRepository;

  private final TicketMapper ticketMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(Long eventId, TicketInsertRequest ticketInsertRequest) throws EventNotFoundException, TicketNotAvailableException {
    var attendeeToUpdate = attendeeService.verifyIfExists(ticketInsertRequest.getAttendeeId());
    var eventToAssociate = eventService.verifyIfExists(eventId);
    var ticketToSave = ticketMapper.toModel(ticketInsertRequest);
    var isPaidTicket = ticketToSave.getType().equals(TicketType.PAID);

    verifyAndDecrementTicketCount(eventToAssociate, isPaidTicket);
    // verifyAndDecrementAttendeeBalance(attendeeToUpdate, eventToAssociate.getTicketPrice());

    ticketToSave.setDate(Instant.now());
    ticketToSave.setPrice(eventToAssociate.getTicketPrice());

    var savedTicket = ticketRepository.save(ticketToSave);
    attendeeToUpdate.getTickets().add(savedTicket);
    eventToAssociate.getTickets().add(savedTicket);
    attendeeRepository.save(attendeeToUpdate);

    return createMessageResponse(DOUBLE_ASSOCIATED_OPERATION_MESSAGE, SAVED, savedTicket.getType() + " " + TICKET,
        savedTicket.getId(), ASSOCIATED.toString(true), EVENT, eventToAssociate.getId(), ATTENDEE, attendeeToUpdate.getId());
  }

  /*
   * GET OPERATION
   */

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(Long eventId, Long ticketId) {
    var eventToUpdate = eventService.verifyIfExists(eventId);
    var ticketToDelete = this.verifyIfExists(ticketId);

    var attendeeToUpdate = attendeeRepository.findByTicketId(ticketId)
        .orElseThrow(() -> new AttendeeNotFoundException(ticketId, true));

    var isPaidTicket = ticketToDelete.getType().equals(TicketType.PAID);
    attendeeToUpdate.setBalance(attendeeToUpdate.getBalance() + ticketToDelete.getPrice());
    verifyAndIncrementTicketCount(eventToUpdate, isPaidTicket);

    ticketRepository.deleteById(ticketId);
    return createMessageResponse(DOUBLE_ASSOCIATED_OPERATION_MESSAGE, DELETED, ticketToDelete.getType() + " " + TICKET,
        ticketToDelete.getId(), ASSOCIATED.toString(true), EVENT, eventToUpdate.getId(), ATTENDEE, attendeeToUpdate.getId());
  }

  /*
   * OTHER
   */

  @Override
  public Ticket verifyIfExists(long id) throws TicketNotFoundException {
    return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
  }

  private void verifyAndDecrementTicketCount(Event event, boolean isPaidTicket) throws TicketNotAvailableException {
    long ticketCount;
    if (isPaidTicket) {
      if ((ticketCount = event.getAmountPaidTicketsAvailable() - 1) < 0)
        throw new TicketNotAvailableException(isPaidTicket);
      event.setAmountPaidTicketsAvailable(ticketCount);
    } else {
      if ((ticketCount = event.getAmountFreeTicketsAvailable() - 1) < 0)
        throw new TicketNotAvailableException(isPaidTicket);
      event.setAmountFreeTicketsAvailable(ticketCount);
    }
  }

  private void verifyAndIncrementTicketCount(Event event, boolean isPaidTicket) {
    if (isPaidTicket) event.setAmountPaidTicketsAvailable(event.getAmountPaidTicketsAvailable() + 1);
    else event.setAmountFreeTicketsAvailable(event.getAmountFreeTicketsAvailable() + 1);
  }

  // private void verifyAndDecrementAttendeeBalance(Attendee attendee, Double priceTicket) {
  //   double balance;
  //   if ((balance = attendee.getBalance() - priceTicket) < 0)
  //     throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Insufficient funds");
  //   attendee.setBalance(balance);
  // }

}
