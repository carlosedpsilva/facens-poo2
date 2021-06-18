package br.facens.poo2.event.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.event.scheduler.dto.info.TicketComponentInfo;
import br.facens.poo2.event.scheduler.dto.info.TicketPageComponentInfo;
import br.facens.poo2.event.scheduler.entity.Ticket;
import br.facens.poo2.event.scheduler.enums.TicketType;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query("SELECT "
      + "t.id AS ticketId,       "
      + "a.id AS attendeeId,     "
      + "a.name AS attendeeName, "
      + "t.type AS type          "
      + "FROM Attendee a, Event e INNER JOIN e.tickets t INNER JOIN a.tickets t2 ON t.id = t2.id "
      + "WHERE e.id = :eventId "
      + "AND (t.type   = :type  OR :type  IS NULL)")
	Page<TicketPageComponentInfo> pageAllByEventId(Pageable pageRequest, @Param("eventId") long eventId,
      @Param("type") TicketType type);

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
