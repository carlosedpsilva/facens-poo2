package br.facens.poo2.ac2project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  // TODO: Implement pageAll query
  Page<Place> pageAll(Pageable pageRequest, Place entityFilter);

}
