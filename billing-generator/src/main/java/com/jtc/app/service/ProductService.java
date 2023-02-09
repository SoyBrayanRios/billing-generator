package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Product;

public interface ProductService {

	public Product saveProduct(Product product) throws Exception;
	public List<Product> getProducts();
	public Product getProductById(Long productId);
	public void deleteProduct(Long productId);
}
