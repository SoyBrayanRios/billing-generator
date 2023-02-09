package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.BillProduct;

public interface BillProductRepository extends JpaRepository<BillProduct, Long> {
	
	@Query(value = "select * from bill_product where bill=:billId ", nativeQuery = true)
	public List<BillProduct> getByBillId(Long billId);
}
