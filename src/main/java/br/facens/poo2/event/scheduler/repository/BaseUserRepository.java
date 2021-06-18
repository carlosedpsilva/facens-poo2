package br.facens.poo2.event.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.facens.poo2.event.scheduler.entity.BaseUser;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> { }