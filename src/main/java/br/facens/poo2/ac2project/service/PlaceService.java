package br.facens.poo2.ac2project.service;

import static br.facens.poo2.ac2project.util.SchedulerUtils.BASIC_MESSAGE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.createMessageResponse;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Entity.PLACE;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.DELETED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.SAVED;
import static br.facens.poo2.ac2project.util.SchedulerUtils.Operation.UPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.PlaceMapper;
import br.facens.poo2.ac2project.dto.request.insert.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.request.update.PlaceUpdateRequest;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.dto.response.PlaceResponse;
import br.facens.poo2.ac2project.entity.Place;
import br.facens.poo2.ac2project.exception.generic.EmptyRequestException;
import br.facens.poo2.ac2project.exception.place.PlaceNotFoundException;
import br.facens.poo2.ac2project.repository.PlaceRepository;
import br.facens.poo2.ac2project.service.meta.SchedulerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceService implements SchedulerService<Place> {

  private final PlaceRepository placeRepository;

  private final PlaceMapper placeMapper;

  /*
   * POST OPERATION
   */

  public MessageResponse save(PlaceInsertRequest placeInsertRequest) {
    var placeToSave = placeMapper.toModel(placeInsertRequest);
    var savedPlace = placeRepository.save(placeToSave);
    return createMessageResponse(BASIC_MESSAGE, SAVED, PLACE, savedPlace.getId());
  }

  /*
   * GET OPERATION
   */

  public Page<PlaceResponse> findAll(Pageable pageRequest, String name, String address) {
    var entityFilter = Place.builder()
        .name( name.isBlank() ? null : name )
        .address( address.isBlank() ? null : address )
        .build();

    var pagedPlaces = placeRepository.pageAll(pageRequest, entityFilter);
    return pagedPlaces.map(placeMapper::toPlaceResponse);
  }

  public PlaceResponse findById(Long id) throws PlaceNotFoundException {
    var savedPlace = verifyIfExists(id);
    return placeMapper.toPlaceResponse(savedPlace);
  }

  /*
   * DELETE OPERATION
   */

  public MessageResponse deleteById(Long id) throws PlaceNotFoundException {
    verifyIfExists(id);
    placeRepository.deleteById(id);
    return createMessageResponse(BASIC_MESSAGE, DELETED, PLACE, id);
  }

  /*
   * PUT OPERATION
   */

  public MessageResponse updateById(Long id, PlaceUpdateRequest placeUpdateRequest) throws PlaceNotFoundException, EmptyRequestException {
    Place placeToUpdate = verifyIfExists(id);

    if (placeUpdateRequest.getName() == null)
          throw new EmptyRequestException();

    placeToUpdate.setName(placeUpdateRequest.getName() == null
        ? placeToUpdate.getName()
        : placeUpdateRequest.getName());

    placeRepository.save(placeToUpdate);
    return createMessageResponse(BASIC_MESSAGE, UPDATED, PLACE, placeToUpdate.getId());
  }

  /*
   * OTHER
   */

  @Override
  public Place verifyIfExists(long id) throws PlaceNotFoundException {
    return placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException(id));
  }

}
