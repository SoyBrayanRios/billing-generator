package com.jtc.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Invoice;
import com.jtc.app.service.InvoiceService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada factura emitida en Faceldi, 
 * cuya información se encuentra guardada en la base de datos de Kosmos.
 *
 */
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	/**
	 * API para guardar en la base de datos de la aplicación una factura previamente guardada en la base de datos 
	 * de Faceldi.
	 * @param invoice (La factura que desea guardar).
	 * @return El objeto con los datos de la factura guardada en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Invoice saveInvoice(@RequestBody Invoice invoice) throws Exception {
		return invoiceService.saveInvoice(invoice);
	}

	/**
	 * API para obtener todas las facturas previamente guardadas en la base de datos de Faceldi y que fueron guardadas
	 * en la base de datos de la aplicación.
	 * @return Lista con los objetos que representan a las facturas de Faceldi guardadas de la base de datos
	 * de Kosmos.
	 */
	@GetMapping("/all")
	public List<Invoice> getInvoices() {
		return invoiceService.getInvoices();
	}

	/*
	@GetMapping("/u")
	public List<Invoice> updateAllInvoices() {
		return invoiceService.updateAllInvoices();
	}
	
	@GetMapping("/u/{year}/{month}")
	public List<Invoice> updateInvoicesByYearMonth(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month) {
		return invoiceService.updateInvoicesByYearMonth(year, month);
	}*/
	
}
