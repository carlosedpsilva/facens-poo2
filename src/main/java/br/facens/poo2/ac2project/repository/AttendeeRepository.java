package br.facens.poo2.ac2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> { }
