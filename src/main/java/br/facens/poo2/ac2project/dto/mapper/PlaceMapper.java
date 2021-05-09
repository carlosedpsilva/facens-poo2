package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.response.PlaceFindResponse;
import br.facens.poo2.ac2project.entity.Place;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    // Requests

    Place toModel (PlaceInsertRequest placeInsertRequest);

    // Responses

    PlaceFindResponse toPlaceFindResponse(Place place);
}
