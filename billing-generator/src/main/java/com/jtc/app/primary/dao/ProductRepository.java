package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
	
	public Product findByProductId(String productId);

}
