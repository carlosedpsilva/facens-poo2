package br.facens.poo2.ac2project.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.facens.poo2.ac2project.dto.request.insert.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.insert.TicketInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.dto.response.EventResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.dto.response.TicketPageableResponse;
import br.facens.poo2.ac2project.dto.response.TicketResponse;
import br.facens.poo2.ac2project.exception.admin.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.event.EventNotFoundException;
import br.facens.poo2.ac2project.exception.event.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.event.IllegalScheduleException;
import br.facens.poo2.ac2project.exception.generic.EmptyRequestException;
import br.facens.poo2.ac2project.exception.generic.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.place.PlaceNotFoundException;
import br.facens.poo2.ac2project.exception.ticket.TicketNotAvailableException;
import br.facens.poo2.ac2project.service.EventService;
import br.facens.poo2.ac2project.service.TicketService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

  private EventService eventService;
  private TicketService ticketService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse save(@RequestBody @Valid EventInsertRequest eventInsertRequest)
      throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException, AdminNotFoundException {
    return eventService.save(eventInsertRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<EventPageableResponse> findAll(
      @RequestParam(value =         "page", defaultValue =   "0")  Integer page,
      @RequestParam(value = "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
      @RequestParam(value =    "direction", defaultValue = "ASC")   String direction,
      @RequestParam(value =      "orderBy", defaultValue =  "id")   String orderBy,
      @RequestParam(value =         "name", defaultValue =    "")   String name,
      @RequestParam(value =  "description", defaultValue =    "")   String description,
      @RequestParam(value =    "startDate", defaultValue =    "")   String startDate
  ) {
    var pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
    return eventService.findAll(pageRequest, name, description, startDate);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public EventResponse findById(@PathVariable Long id) throws EventNotFoundException {
    return eventService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse deleteById(@PathVariable Long id) throws EventNotFoundException {
    return eventService.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse updateById(@PathVariable Long id, @RequestBody @Valid EventUpdateRequest eventUpdateRequest)
      throws EventNotFoundException, EmptyRequestException {
    return eventService.updateById(id, eventUpdateRequest);
  }

  @PostMapping("/{eventId}/places/{placeId}")
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse associatePlaceById(@PathVariable Long eventId, @PathVariable Long placeId)
      throws EventNotFoundException, PlaceNotFoundException, EventScheduleNotAvailableException{
    return eventService.associatePlaceById(eventId, placeId);
  }

  @DeleteMapping("/{eventId}/places/{placeId}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse deassociatePlaceById(@PathVariable Long eventId, @PathVariable Long placeId)
      throws EventNotFoundException, PlaceNotFoundException, EventScheduleNotAvailableException{
    return eventService.deassociatePlaceById(eventId, placeId);
  }

  @PostMapping("/{eventId}/tickets")
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse saveTicketById(@PathVariable Long eventId,
      @RequestBody @Valid TicketInsertRequest ticketInsertRequest)
      throws EventNotFoundException, TicketNotAvailableException {
    return ticketService.save(eventId, ticketInsertRequest);
  }

  @GetMapping("/{eventId}/tickets")
  @ResponseStatus(HttpStatus.OK)
  public TicketPageableResponse findAllTickets(@PathVariable Long eventId,
      @RequestParam(value =         "page", defaultValue =   "0")  Integer page,
      @RequestParam(value = "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
      @RequestParam(value =    "direction", defaultValue = "ASC")   String direction,
      @RequestParam(value =      "orderBy", defaultValue =  "id")   String orderBy,
      @RequestParam(value =         "type", defaultValue =    "")   String type,
      @RequestParam(value =        "price", defaultValue =   "0")   String price
  ) {
    var pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
    return ticketService.findByEventId(pageRequest, eventId, type, price);
  }

  @GetMapping("/{eventId}/tickets/{ticketId}")
  @ResponseStatus(HttpStatus.OK)
  public TicketResponse findTicketById(@PathVariable Long eventId, @PathVariable Long ticketId) {
    return ticketService.findById(eventId, ticketId);
  }

  @DeleteMapping("/{eventId}/tickets/{ticketId}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse deleteTicketById(@PathVariable Long eventId, @PathVariable Long ticketId)
      throws EventNotFoundException, TicketNotAvailableException {
    return ticketService.deleteById(eventId, ticketId);
  }

}
