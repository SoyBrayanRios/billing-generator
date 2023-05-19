package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Product;
import com.jtc.app.service.ProductService;

/**
 * Controlador que expone todas las API que interactuan con la información de los productos con los que se facturan 
 * los servicios de FE, NE y DS.
 *
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/**
	 * API para guardar un producto de facturación.
	 * @param product (El producto que se desea guardar).
	 * @return El objeto con los datos del producto guardado en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Product saveProduct(Product product) throws Exception {
		return productService.saveProduct(product);
	}

	/**
	 * API para obtener la información de los productos de facturación existentes.
	 * @return Lista con los objetos que representan a los productos de facturación de la base de datos.
	 */
	@GetMapping("/")
	public List<Product> getProducts() {
		return productService.getProducts();
	}

}
