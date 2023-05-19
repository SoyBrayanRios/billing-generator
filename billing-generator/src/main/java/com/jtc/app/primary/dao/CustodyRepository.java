package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.Custody;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "custody".
 *
 */
public interface CustodyRepository extends JpaRepository<Custody, Long> {

}
