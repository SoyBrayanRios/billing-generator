package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	public Product findByProductId(Long productId);

}
