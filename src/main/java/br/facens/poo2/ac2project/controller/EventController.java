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

import br.facens.poo2.ac2project.dto.request.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.request.TicketInsertRequest;
import br.facens.poo2.ac2project.dto.response.EventFindResponse;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.exception.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.EventNotFoundException;
import br.facens.poo2.ac2project.exception.EventScheduleNotAvailableException;
import br.facens.poo2.ac2project.exception.IllegalDateTimeFormatException;
import br.facens.poo2.ac2project.exception.IllegalScheduleException;
import br.facens.poo2.ac2project.exception.PlaceNotFoundException;
import br.facens.poo2.ac2project.exception.TicketNotAvailableException;
import br.facens.poo2.ac2project.service.EventService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

  private EventService eventService;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse save(@RequestBody @Valid EventInsertRequest eventInsertRequest)
      throws IllegalScheduleException, IllegalDateTimeFormatException, EventScheduleNotAvailableException, AdminNotFoundException {
    return eventService.save(eventInsertRequest);
  }


  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<EventPageableResponse> findAll(
      @RequestParam(value =          "page", defaultValue =   "0")  Integer page,
      @RequestParam(value =  "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
      @RequestParam(value =     "direction", defaultValue = "ASC")   String direction,
      @RequestParam(value =       "orderBy", defaultValue =  "id")   String orderBy,
      @RequestParam(value =          "name", defaultValue =    "")   String name,
      @RequestParam(value =   "description", defaultValue =    "")   String description,
      @RequestParam(value =     "startDate", defaultValue =    "")   String startDate
  ) {
    PageRequest pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
    return eventService.findAll(pageRequest, name, description, startDate);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public EventFindResponse findById(@PathVariable Long id) throws EventNotFoundException {
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
  public MessageResponse associatePlaceById(@PathVariable Long eventId, @PathVariable Long placeId) throws EventNotFoundException, PlaceNotFoundException, EventScheduleNotAvailableException{
    return eventService.associatePlaceById(eventId, placeId);
  }

  @PostMapping("/{eventId}/tickets")
  public MessageResponse associateTicketById(@PathVariable Long eventId, @RequestBody @Valid TicketInsertRequest ticketInsertRequest) throws EventNotFoundException, TicketNotAvailableException {
    return eventService.saveTicket(eventId, ticketInsertRequest);
  }

}
