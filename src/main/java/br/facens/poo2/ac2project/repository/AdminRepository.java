package br.facens.poo2.ac2project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.facens.poo2.ac2project.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Page<Admin> pageAll(Pageable pageRequest, Admin entityFilter); }