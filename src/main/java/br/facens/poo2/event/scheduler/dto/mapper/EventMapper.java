package br.facens.poo2.event.scheduler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.facens.poo2.event.scheduler.dto.request.insert.EventInsertRequest;
import br.facens.poo2.event.scheduler.dto.request.update.EventUpdateRequest;
import br.facens.poo2.event.scheduler.dto.response.EventPageableResponse;
import br.facens.poo2.event.scheduler.dto.response.EventResponse;
import br.facens.poo2.event.scheduler.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

  /*
   * Request
   */

  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "endDate", source = "endDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endTime", dateFormat = "HH:mm")
  @Mapping(target = "amountFreeTicketsAvailable", source = "amountFreeTickets")
  @Mapping(target = "amountPaidTicketsAvailable", source = "amountPaidTickets")
  Event toModel(EventInsertRequest eventInsertRequest);

  Event toModel(EventUpdateRequest eventUpdateRequest);

  /*
   * Responss
   */

  @Mapping(target = "eventId", source = "event.id")
  @Mapping(target = "adminId", source = "event.admin.id")
  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "endDate", source = "endDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endTime", dateFormat = "HH:mm")
  EventResponse toEventFindResponse(Event event);

  @Mapping(target = "eventId", source = "event.id")
  @Mapping(target = "adminId", source = "event.admin.id")
  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  EventPageableResponse toEventPageableResponse(Event event);

}
