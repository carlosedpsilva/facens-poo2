package br.facens.poo2.ac2project.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.facens.poo2.ac2project.dto.request.PlaceInsertRequest;
import br.facens.poo2.ac2project.dto.request.PlaceUpdateRequest;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.dto.response.PlaceResponse;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.exception.PlaceNotFoundException;
import br.facens.poo2.ac2project.service.PlaceService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/places")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlaceController {
    private PlaceService placeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse save(@RequestBody @Valid PlaceInsertRequest placeInsertRequest) {
      return placeService.save(placeInsertRequest);
    }
  
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PlaceResponse> findAll(
        @RequestParam(value =          "page", defaultValue =   "0")  Integer page,
        @RequestParam(value =  "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
        @RequestParam(value =     "direction", defaultValue = "ASC")   String direction,
        @RequestParam(value =       "orderBy", defaultValue =  "id")   String orderBy,
        @RequestParam(value =          "name", defaultValue =    "")   String name,
        @RequestParam(value =       "address", defaultValue =    "")   String address
    ) {
      PageRequest pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
      return placeService.findAll(pageRequest, name, address);
    }
  
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaceResponse findById(@PathVariable Long id) throws PlaceNotFoundException {
      return placeService.findById(id);
    }
  
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse deleteById(@PathVariable Long id) throws PlaceNotFoundException {
      return placeService.deleteById(id);
    }
  
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse updateById(@PathVariable Long id, @RequestBody @Valid PlaceUpdateRequest placeUpdateRequest)
        throws PlaceNotFoundException, EmptyRequestException {
      return placeService.updateById(id, placeUpdateRequest);
    }
}
