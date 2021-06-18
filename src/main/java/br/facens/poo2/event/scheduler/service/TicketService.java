package br.facens.poo2.event.scheduler.service;

import static br.facens.poo2.event.scheduler.util.SchedulerUtils.DOUBLE_ASSOCIATED_OPERATION_MESSAGE;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Entity.ATTENDEE;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Entity.EVENT;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Entity.TICKET;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.ASSOCIATED;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.event.scheduler.util.SchedulerUtils.Operation.SAVED;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.event.scheduler.dto.mapper.TicketMapper;
import br.facens.poo2.event.scheduler.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import br.facens.poo2.event.scheduler.dto.response.TicketPageableResponse;
import br.facens.poo2.event.scheduler.dto.response.TicketResponse;
import br.facens.poo2.event.scheduler.entity.Event;
import br.facens.poo2.event.scheduler.entity.Ticket;
import br.facens.poo2.event.scheduler.enums.TicketType;
import br.facens.poo2.event.scheduler.exception.attendee.AttendeeNotFoundException;
import br.facens.poo2.event.scheduler.exception.event.EventNotFoundException;
import br.facens.poo2.event.scheduler.exception.ticket.TicketNotAvailableException;
import br.facens.poo2.event.scheduler.exception.ticket.TicketNotFoundException;
import br.facens.poo2.event.scheduler.repository.AttendeeRepository;
import br.facens.poo2.event.scheduler.repository.TicketRepository;
import br.facens.poo2.event.scheduler.service.meta.SchedulerService;
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
    var eventToAssociate = eventService.verifyIfExists(eventId);
    var attendeeToUpdate = attendeeService.verifyIfExists(ticketInsertRequest.getAttendeeId());
    var ticketToSave = ticketMapper.toModel(ticketInsertRequest);
    var isPaidTicket = ticketToSave.getType().equals(TicketType.PAID);

    verifyAndDecrementTicketCount(eventToAssociate, isPaidTicket);

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

  public TicketPageableResponse findByEventId(Pageable pageRequest, long eventId, String type) {
    var savedEvent = eventService.verifyIfExists(eventId);
    var typeFilter = type.matches("^(?:FREE|PAID)$") ? TicketType.valueOf(type) : null;
    var pagedTickets = ticketRepository.pageAllByEventId(pageRequest, eventId, typeFilter);

    return TicketPageableResponse.builder()
        .eventId(savedEvent.getId())
        .ticketPrice(savedEvent.getTicketPrice())
        .amountFreeTicketsAvailable(savedEvent.getAmountFreeTicketsAvailable())
        .amountPaidTicketsAvailable(savedEvent.getAmountPaidTicketsAvailable())
        .amountFreeTicketsSold(savedEvent.getAmountFreeTicketsSold())
        .amountPaidTicketsSold(savedEvent.getAmountPaidTicketsSold())
        .tickets(pagedTickets.map(ticketMapper::toTicketPageComponentResponse))
        .build();
  }

  public TicketResponse findById(Long eventId, Long ticketId) {
    eventService.verifyIfExists(eventId);
    var ticketComponentInfo = ticketRepository.findById(eventId, ticketId);
    return ticketMapper.toTicketResponse(ticketComponentInfo, LocalDateTime.ofInstant(ticketComponentInfo.getDate(), ZoneId.of("America/Sao_Paulo")));
  }

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

}
