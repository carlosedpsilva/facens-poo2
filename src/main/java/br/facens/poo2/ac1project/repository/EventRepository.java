package br.facens.poo2.ac1project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac1project.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e WHERE "
      + "    (LOWER(e.name)        LIKE LOWER(CONCAT('%',        :#{#req.name}, '%')) OR :#{#req.name}        IS NULL) "
      + "AND (LOWER(e.place)       LIKE LOWER(CONCAT('%',       :#{#req.place}, '%')) OR :#{#req.place}       IS NULL) "
      + "AND (LOWER(e.description) LIKE LOWER(CONCAT('%', :#{#req.description}, '%')) OR :#{#req.description} IS NULL) "
      + "AND ((e.startDate >= :#{#req.startDate}) OR :#{#req.startDate} IS NULL) "
      )
  public Page<Event> pageAll(Pageable pageRequest, @Param("req") Event event);

  @Query("SELECT e FROM Event e WHERE "
      + "( e.name = :#{#req.name} AND e.place = :#{#req.place} ) "
      + "AND (( :#{#req.startDate} <  e.startDate  AND :#{#req.endDate} > e.startDate ) "
      + "OR   ( :#{#req.startDate} >= e.startDate  AND :#{#req.startDate} < e.endDate ) "
      + "OR   ( :#{#req.startDate} =  e.startDate  AND :#{#req.startDate} = e.endDate   "
          + "AND (( :#{#req.startTime} <  e.startTime AND :#{#req.endTime} > e.startTime )   "
          + "OR   ( :#{#req.startTime} >= e.startTime AND :#{#req.startTime} < e.endTime )   "
          + "OR   ( :#{#req.startTime} =  e.startTime AND :#{#req.startTime} = e.endTime ))))")
  public List<Event> findEventsBySchedule(@Param("req") Event event);

}
