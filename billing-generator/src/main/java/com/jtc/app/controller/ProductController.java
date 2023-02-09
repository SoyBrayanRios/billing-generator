package com.jtc.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Product;
import com.jtc.app.service.ProductService;

@RestController
public class ProductController {

	private ProductService productService;
	
	public Product saveProduct(Product product) throws Exception {
		return productService.saveProduct(product);
	}

	public List<Product> getProducts() {
		return productService.getProducts();
	}

	public Product getProductById(Long productId) {
		return productService.getProductById(productId);
	}

	public void deleteProduct(Long productId) {
		productService.deleteProduct(productId);
	}
}
