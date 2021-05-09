package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.AttendeeInsertRequest;
import br.facens.poo2.ac2project.dto.response.AttendeeFindResponse;
import br.facens.poo2.ac2project.entity.Attendee;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {
    
    // Requests

    Attendee toModel (AttendeeInsertRequest attendeeInsertRequest);

    // Responses

    AttendeeFindResponse toAttendeeFindResponse(Attendee attendee);
}
