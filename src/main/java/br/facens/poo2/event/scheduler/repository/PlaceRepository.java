package br.facens.poo2.event.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.event.scheduler.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  @Query("SELECT e FROM Place e WHERE "
      + "    (LOWER(e.name)    LIKE LOWER(CONCAT('%',    CAST(:#{#req.name} AS string), '%')) OR :#{#req.name}    IS NULL) "
      + "AND (LOWER(e.address) LIKE LOWER(CONCAT('%', CAST(:#{#req.address} AS string), '%')) OR :#{#req.address} IS NULL) ")
  Page<Place> pageAll(Pageable pageRequest, @Param("req") Place entitySearchFilter);

}
