package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Product;

/**
 * Esta interface define los servicios relacionados a la clase Product.
 *
 */
public interface ProductService {

	public Product saveProduct(Product product) throws Exception;
	public List<Product> getProducts();
	public Product getProductById(String productId);
	public void deleteProduct(String productId);
}
