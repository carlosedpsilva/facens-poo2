package br.facens.poo2.ac2project.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

  @Query("SELECT e FROM Admin e WHERE "
      + "    (LOWER(e.name)        LIKE LOWER(CONCAT('%',        CAST(:#{#req.name} AS string), '%')) OR :#{#req.name}        IS NULL) "
      + "AND (LOWER(e.email)       LIKE LOWER(CONCAT('%',       CAST(:#{#req.email} AS string), '%')) OR :#{#req.email}       IS NULL) "
      + "AND (LOWER(e.phoneNumber) LIKE LOWER(CONCAT('%', CAST(:#{#req.phoneNumber} AS string), '%')) OR :#{#req.phoneNumber} IS NULL) ")
  Page<Admin> pageAll(Pageable pageRequest, @Param("req") Admin entitySearchFilter);

  Optional<Admin> findByEmail(String email);

}
