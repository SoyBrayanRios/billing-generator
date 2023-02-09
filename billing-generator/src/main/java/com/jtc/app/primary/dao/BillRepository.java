package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

	@Query(value = "select * from bill where \"year\"=:year and \"month\"=:month ", nativeQuery = true)
	public List<Bill> getFaceldiReport(Long year, Long month);
	
}
