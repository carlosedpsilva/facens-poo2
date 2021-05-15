package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.AttendeeInsertRequest;
import br.facens.poo2.ac2project.dto.request.AttendeeUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AttendeeResponse;
import br.facens.poo2.ac2project.entity.Attendee;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {
    
    // Requests

    Attendee toModel (AttendeeInsertRequest attendeeInsertRequest);
    Attendee toModel (AttendeeUpdateRequest attendeeUpdateRequest);

    // Responses

    AttendeeResponse toAttendeeFindResponse(Attendee attendee);
    
}
