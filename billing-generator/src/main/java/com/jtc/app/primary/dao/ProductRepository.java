package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.Product;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "product".
 *
 */
public interface ProductRepository extends JpaRepository<Product, String> {
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener un producto de facturación según un ID dado. 
	 * @param productId (ID del producto).
	 * @return El objeto con los datos del producto.
	 */
	public Product findByProductId(String productId);

}
