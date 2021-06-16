package br.facens.poo2.ac2project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.dto.info.TicketComponentInfo;
import br.facens.poo2.ac2project.dto.info.TicketPageComponentInfo;
import br.facens.poo2.ac2project.entity.Ticket;
import br.facens.poo2.ac2project.enums.TicketType;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query("SELECT "
      + "t.id AS ticketId,       "
      + "a.id AS attendeeId,     "
      + "a.name AS attendeeName, "
      + "t.type AS type,         "
      + "t.price AS price        "
      + "FROM Attendee a, Event e INNER JOIN e.tickets t INNER JOIN a.tickets t2 ON t.id = t2.id "
      + "WHERE e.id = :eventId "
      + "AND (t.type   = :type  OR :type  IS NULL) "
      + "AND (t.price >= :price OR :price IS NULL) ")
	Page<TicketPageComponentInfo> pageAllByEventId(Pageable pageRequest, @Param("eventId") long eventId,
      @Param("type") TicketType type, @Param("price") Double price);

  @Query("SELECT "
      + "t.id    AS ticketId,     "
      + "e.id    AS eventId,      "
      + "a.id    AS attendeeId,   "
      + "a.name  AS attendeeName, "
      + "t.date  AS date,         "
      + "t.type  AS type,         "
      + "t.price AS price         "
      + "FROM Attendee a, Event e INNER JOIN e.tickets t INNER JOIN a.tickets t2 ON t.id = t2.id "
      + "WHERE e.id = :eventId AND t.id = :ticketId")
  TicketComponentInfo findById(long eventId, long ticketId);

}
