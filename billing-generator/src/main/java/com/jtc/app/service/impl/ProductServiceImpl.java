package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.ProductRepository;
import com.jtc.app.primary.entity.Product;
import com.jtc.app.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Product saveProduct(Product product) throws Exception {
		return productRepository.save(product);
	}

	@Override
	public List<Product> getProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product getProductById(Long productId) {
		return productRepository.getById(productId);
	}

	@Override
	public void deleteProduct(Long productId) {
		productRepository.deleteById(productId);
	}

}
