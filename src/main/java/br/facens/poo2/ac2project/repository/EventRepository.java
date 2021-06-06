package br.facens.poo2.ac2project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Event;
import br.facens.poo2.ac2project.entity.Place;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e WHERE "
      + "((e.startDate >= :#{#req.startDate}) OR CAST(CAST(:#{#req.startDate} AS string) AS date) IS NULL) "
      + "AND (LOWER(e.name)        LIKE LOWER(CONCAT('%',        CAST(:#{#req.name} AS string), '%')) OR :#{#req.name}        IS NULL) "
      + "AND (LOWER(e.place)       LIKE LOWER(CONCAT('%',       CAST(:#{#req.place} AS string), '%')) OR :#{#req.place}       IS NULL) "
      + "AND (LOWER(e.description) LIKE LOWER(CONCAT('%', CAST(:#{#req.description} AS string), '%')) OR :#{#req.description} IS NULL) "
      )
  public Page<Event> pageAll(Pageable pageRequest, @Param("req") Event event);

  @Query("SELECT e FROM Event e "
      + "INNER JOIN e.places p "
      + "WHERE p.id = :#{#reqPlace.id} "
      + "AND (( :#{#reqEvent.startDate} <  e.startDate  AND :#{#reqEvent.endDate} > e.startDate ) "
      + "OR   ( :#{#reqEvent.startDate} >= e.startDate  AND :#{#reqEvent.startDate} < e.endDate ) "
      + "OR   ( :#{#reqEvent.startDate} =  e.startDate  AND :#{#reqEvent.startDate} = e.endDate   "
          + "AND (( :#{#reqEvent.startTime} <  e.startTime AND :#{#reqEvent.endTime} > e.startTime )   "
          + "OR   ( :#{#reqEvent.startTime} >= e.startTime AND :#{#reqEvent.startTime} < e.endTime )   "
          + "OR   ( :#{#reqEvent.startTime} =  e.startTime AND :#{#reqEvent.startTime} = e.endTime ))))")
  public List<Event> findEventsBySchedule(@Param("reqEvent") Event event, @Param("reqPlace") Place place);

}
