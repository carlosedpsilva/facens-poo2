package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.insert.AttendeeInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.AttendeeUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AttendeeResponse;
import br.facens.poo2.ac2project.entity.Attendee;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {

  /*
   * Request
   */

  Attendee toModel(AttendeeInsertRequest attendeeInsertRequest);

  Attendee toModel(AttendeeUpdateRequest attendeeUpdateRequest);

  /*
   * Response
   */

  AttendeeResponse toAttendeeFindResponse(Attendee attendee);

}
