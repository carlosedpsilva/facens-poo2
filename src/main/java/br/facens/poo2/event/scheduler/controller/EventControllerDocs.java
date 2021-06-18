package br.facens.poo2.event.scheduler.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import br.facens.poo2.event.scheduler.dto.request.insert.EventInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.EventUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.EventPageableResponse;
import br.facens.poo2.event.scheduler.dto.response.EventResponse;
import br.facens.poo2.event.scheduler.dto.response.MessageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages Events")
public interface EventControllerDocs {

  @ApiOperation(value = "Event schedule operation")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Event successfully scheduled."),
      @ApiResponse(code = 400, message = "Missing required fields or wrong field range value."),
      @ApiResponse(code = 409, message = "Event already scheduled for the specified date-time.")
  })
  MessageResponse save(EventInsertRequest eventInsertRequest);

  @ApiOperation(value = "List all scheduled events")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully listed all scheduled events")
  })
  Page<EventPageableResponse> findAll(
    @RequestParam(value =          "page", defaultValue =   "0")  Integer page,
    @RequestParam(value =  "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
    @RequestParam(value =     "direction", defaultValue = "ASC")   String direction,
    @RequestParam(value =       "orderBy", defaultValue =  "id")   String orderBy,
    @RequestParam(value =          "name", defaultValue =    "")   String name,
    @RequestParam(value =   "description", defaultValue =    "")   String description,
    @RequestParam(value =     "startDate", defaultValue =    "")   String startDate
  );

  @ApiOperation(value = "Return an scheduled event found by a given id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully found event in the system"),
      @ApiResponse(code = 404, message = "Event with given id not found")
  })
  EventResponse findById(Long id);

  @ApiOperation(value = "Delete a scheduled event by a given id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully deleted event in the system"),
      @ApiResponse(code = 404, message = "Event with given id not found")
  })
  MessageResponse deleteById(Long id);

  @ApiOperation(value = "Update a scheduled event info by a given id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully updated event in the system"),
      @ApiResponse(code = 404, message = "Event with given id not found")
  })
  MessageResponse updateById(Long id, EventUpdateRequest eventUpdateRequest);

}
