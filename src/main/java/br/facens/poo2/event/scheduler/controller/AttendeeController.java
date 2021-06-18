package br.facens.poo2.event.scheduler.controller;

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

import br.facens.poo2.event.scheduler.dto.request.insert.AttendeeInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.AttendeeUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.AttendeeResponse;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import br.facens.poo2.event.scheduler.exception.attendee.AttendeeNotFoundException;
import br.facens.poo2.event.scheduler.service.AttendeeService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/attendees")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AttendeeController {

  private AttendeeService attendeeService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse save(@RequestBody @Valid AttendeeInsertRequest attendeeInsertRequest) {
    return attendeeService.save(attendeeInsertRequest);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<AttendeeResponse> findAll(
      @RequestParam(value =          "page", defaultValue =   "0")  Integer page,
      @RequestParam(value =  "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
      @RequestParam(value =     "direction", defaultValue = "ASC")   String direction,
      @RequestParam(value =       "orderBy", defaultValue =  "id")   String orderBy,
      @RequestParam(value =          "name", defaultValue =    "")   String name,
      @RequestParam(value =         "email", defaultValue =    "")   String email,
      @RequestParam(value =     "startDate", defaultValue =    "")   Double balance
  ) {
    PageRequest pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
    return attendeeService.findAll(pageRequest, name, email, balance);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AttendeeResponse findById(@PathVariable Long id) throws AttendeeNotFoundException {
    return attendeeService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse deleteById(@PathVariable Long id) throws AttendeeNotFoundException {
    return attendeeService.deleteById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponse updateById(@PathVariable Long id,
      @RequestBody @Valid AttendeeUpdateRequest attendeeUpdateRequest) throws AttendeeNotFoundException {
    return attendeeService.updateById(id, attendeeUpdateRequest);
  }

}
