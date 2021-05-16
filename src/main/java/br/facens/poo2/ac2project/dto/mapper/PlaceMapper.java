package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.request.PlaceUpdateRequest;
import br.facens.poo2.ac2project.dto.response.PlaceResponse;
import br.facens.poo2.ac2project.entity.Place;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

  /*
   * Requests
   */

  Place toModel(PlaceInsertRequest placeInsertRequest);

  Place toModel(PlaceUpdateRequest placeUpdateRequest);

  /*
   * Responses
   */

  PlaceResponse toPlaceResponse(Place place);

}
