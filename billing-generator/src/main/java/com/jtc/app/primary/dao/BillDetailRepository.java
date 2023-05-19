package com.jtc.app.primary.dao;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "bill_resume".
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.BillDetail;

public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
	
}
