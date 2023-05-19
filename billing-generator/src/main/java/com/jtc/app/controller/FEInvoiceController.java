package com.jtc.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.FEInvoice;
import com.jtc.app.service.FEInvoiceService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada factura de FE, DS y NE 
 * directamente en la base de datos de Faceldi.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/invoice")
public class FEInvoiceController {

	@Autowired
	private FEInvoiceService feInvoiceService;
	
	/**
	 * API que obtiene la fecha del primer documento emitido de una sucursal.
	 * @param branchId (ID de la sucursal).
	 * @return La fecha de primera emisión de una sucursal.
	 */
	@GetMapping("/first/{branchId}")
	public Date getFirstIssuedDate(@PathVariable(name = "branchId") Long branchId) {
		return feInvoiceService.getFirstIssuedDate(branchId);
	}
	
}
