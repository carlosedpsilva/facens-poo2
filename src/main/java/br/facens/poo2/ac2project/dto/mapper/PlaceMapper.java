package br.facens.poo2.ac2project.dto.mapper;

import org.mapstruct.Mapper;

import br.facens.poo2.ac2project.dto.request.insert.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.PlaceUpdateRequest;
import br.facens.poo2.ac2project.dto.response.PlaceResponse;
import br.facens.poo2.ac2project.entity.Place;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

  /*
   * Request
   */

  Place toModel(PlaceInsertRequest placeInsertRequest);

  Place toModel(PlaceUpdateRequest placeUpdateRequest);

  /*
   * Response
   */

  PlaceResponse toPlaceResponse(Place place);

}
