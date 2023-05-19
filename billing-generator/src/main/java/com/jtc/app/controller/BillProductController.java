package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.service.BillProductService;

/**
 * Controlador que expone todas las API que interactuan con la información de la alianzas.
 *
 */
@RestController
@RequestMapping("/api/bill-product")
public class BillProductController {

	@Autowired
	private BillProductService billProductService;
	
	/**
	 * API para guardar el detalle de una factura generada por la aplicación.
	 * @param billProduct (El detalle de la factura que se desea guardar).
	 * @return El objeto con los datos del detalle de la factura guardado en la base de datos.
	 * @throws Exception
	 */
	public BillProduct saveBillProduct(@RequestBody BillProduct billProduct) throws Exception {
		return billProductService.saveBillProduct(billProduct);
	}

	/**
	 * API para obtener el detalle todas las facturas genradas por la aplicación y que se 
	 * encuentran guardadas en la base de datos.
	 * @return Lista de objetos de tipo BillProduct que contienen los datos del detalle de las facturas
	 * guardadas en la base de datos.
	 */
	@GetMapping("/")
	public List<BillProduct> getBillProducts() {
		return billProductService.getBillProducts();
	}

	/**
	 * API para obtener los datos del detalle de una factura según su ID.
	 * @param billProductId (ID del detalle de la factura)
	 * @return El objeto con los datos del detalle de la factura guardado en la base de datos.
	 */
	public BillProduct getBillProductById(Long billProductId) {
		return billProductService.getBillProductById(billProductId);
	}
	
}
