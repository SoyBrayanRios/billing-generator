package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.BillDetail;

public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
	
}
