package br.facens.poo2.ac2project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

  @Query("SELECT e FROM Attendee e WHERE "
      + "((e.balance >= :#{#req.balance}) OR :#{#req.balance} IS NULL) "
      + "AND (LOWER(e.name)  LIKE LOWER(CONCAT('%',  CAST(:#{#req.name} AS string), '%')) OR :#{#req.name}  IS NULL) "
      + "AND (LOWER(e.email) LIKE LOWER(CONCAT('%', CAST(:#{#req.email} AS string), '%')) OR :#{#req.email} IS NULL) ")
  Page<Attendee> pageAll(Pageable pageRequest, @Param("req") Attendee entitysSearchFilter);

}
