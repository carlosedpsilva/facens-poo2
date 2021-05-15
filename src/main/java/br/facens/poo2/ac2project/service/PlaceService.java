package br.facens.poo2.ac2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.facens.poo2.ac2project.dto.mapper.PlaceMapper;
import br.facens.poo2.ac2project.dto.request.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.request.PlaceUpdateRequest;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.dto.response.PlaceResponse;
import br.facens.poo2.ac2project.entity.Place;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.PlaceNotFoundException;
import br.facens.poo2.ac2project.repository.PlaceRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceService {
    
    private static final String SAVED_MESSAGE = "Saved Place with ID ";
    private static final String DELETED_MESSAGE = "Deleted Place with ID ";
    private static final String UPDATED_MESSAGE = "Updated Place with ID ";
  
    private PlaceRepository placeRepository;
  
    private PlaceMapper placeMapper;
  
    // POST 
  
    public MessageResponse save(PlaceInsertRequest placeInsertRequest) {
    
        Place placeToSave = placeMapper.toModel(placeInsertRequest);
  
        Place savedPlace = placeRepository.save(placeToSave);
        return createMessageResponse(savedPlace.getId(), SAVED_MESSAGE); 
    }
    
    // GET
    
    public Page<PlaceResponse> findAll(Pageable pageRequest,
        String name, String address) {
  
      Place entityFilter = Place.builder()
          .name( name.isBlank() ? null : name )
          .address( address.isBlank() ? null : address )
          .build();
  
      Page<Place> pagedPlaces = placeRepository.pageAll(pageRequest, entityFilter);
      return pagedPlaces.map(placeMapper::toPlaceResponse);
    }
    
    public PlaceResponse findById(Long id) throws PlaceNotFoundException {
      Place savedPlace = verifyIfExists(id);
      return placeMapper.toPlaceResponse(savedPlace);
    }
  
    // DELETE
  
    public MessageResponse deleteById(Long id) throws PlaceNotFoundException {
      verifyIfExists(id);
      placeRepository.deleteById(id);
      return createMessageResponse(id, DELETED_MESSAGE);
    }
  
    // PUT
  
    public MessageResponse updateById(Long id, PlaceUpdateRequest placeUpdateRequest) throws PlaceNotFoundException, EmptyRequestException {
      Place placeToUpdate = verifyIfExists(id);
  
      if (placeUpdateRequest.getName().isEmpty())
            throw new EmptyRequestException();
  
      placeToUpdate.setName(placeUpdateRequest.getName().isEmpty() ? placeToUpdate.getName() : placeUpdateRequest.getName());
  
      placeRepository.save(placeToUpdate);
      return createMessageResponse(placeToUpdate.getId(), UPDATED_MESSAGE);
    }
  
    // METHODS
  
    private Place verifyIfExists(Long id) throws PlaceNotFoundException {
      return placeRepository.findById(id).orElseThrow(() -> new PlaceNotFoundException(id));
    }
  
    private MessageResponse createMessageResponse(Long id, String message) {
      return MessageResponse.builder()
          .message(message + id)
          .build();
    }
}