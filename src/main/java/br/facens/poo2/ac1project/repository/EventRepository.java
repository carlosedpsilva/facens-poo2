package br.facens.poo2.ac1project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac1project.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> { }
