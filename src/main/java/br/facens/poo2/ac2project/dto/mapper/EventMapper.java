package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.facens.poo2.ac2project.dto.request.EventInsertRequest;
import br.facens.poo2.ac2project.dto.request.EventUpdateRequest;
import br.facens.poo2.ac2project.dto.response.EventFindResponse;
import br.facens.poo2.ac2project.dto.response.EventPageableResponse;
import br.facens.poo2.ac2project.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

  /*
   * Requests
   */

  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "endDate", source = "endDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endTime", dateFormat = "HH:mm")
  Event toModel(EventInsertRequest eventInsertRequest);

  Event toModel(EventUpdateRequest eventUpdateRequest);

  /*
   * Responses
   */

  @Mapping(target = "eventId", source = "event.id")
  @Mapping(target = "adminId", source = "event.admin.id")
  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "endDate", source = "endDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endTime", dateFormat = "HH:mm")
  EventFindResponse toEventFindResponse(Event event);

  @Mapping(target = "eventId", source = "event.id")
  @Mapping(target = "adminId", source = "event.admin.id")
  @Mapping(target = "startDate", source = "startDate", dateFormat = "dd/MM/yyyy")
  @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
  EventPageableResponse toEventPageableResponse(Event event);

}
