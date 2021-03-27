package br.facens.poo2.ac1project.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac1project.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e")
  public Page<Event> pageAll(Pageable pageRequest);

  @Query("SELECT e FROM Event e WHERE "
      + "( e.name = :#{#req.name} ) AND ( e.place = :#{#req.place} ) "
      + "AND ( :#{#req.startDate} < e.startDate  AND :#{#req.endDate} > e.startDate ) "
      + "OR  ( :#{#req.startDate} >= e.startDate AND :#{#req.startDate} < e.endDate ) "
      + "OR  ( :#{#req.startDate} = e.startDate  AND :#{#req.startDate} = e.endDate   "
          + "AND (( :#{#req.startTime} <  e.startTime AND :#{#req.endTime} > e.startTime )  "
          + "OR   ( :#{#req.startTime} >= e.startTime AND :#{#req.startTime} < e.endTime )  "
          + "OR   ( :#{#req.startTime} =  e.startTime AND :#{#req.startTime} = e.endTime )))")
  public Optional<Event> findEvent(@Param("req") Event event);

}
