package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.InvoiceResume;
import com.jtc.app.service.InvoiceResumeService;

/**
 * Controlador que expone todas las API que interactuan con la información general de los conteos de 
 * facturas emitidas en Faceldi.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/inv-resume")
public class InvoiceResumeController {
	
	@Autowired
	private InvoiceResumeService invoiceResumeService;
	
	/**
	 * API para obtener todas los conteos de facturas de la base de datos.
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan los conteos de facturas guardados en la base de datos.
	 */
	@GetMapping("/all/{module}")
	public List<InvoiceResume> getAllResumes(@PathVariable("module") String module) {
		return invoiceResumeService.getAllResumes(module);
	}
	
	/*
	@GetMapping("/u/{module}")
	public List<InvoiceResume> updateInvoiceResumes(@PathVariable("module") String module) {
		return invoiceResumeService.getAllResumes(module);
	}*/
	
	/**
	 * API para obtener los conteos de facturas específicos de una sucursal según los parámetros dados.
	 * @param branchId (ID de la sucursal).
	 * @param year (Año del conteo requerido).
	 * @param month (Mes del conteo requerido).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return El objeto con los datos del conteo de facturas requerido.
	 */
	@GetMapping("/{branchId}/{year}/{month}/{module}")
	public InvoiceResume getInvoiceResumeRow(@PathVariable("branchId") Long branchId, @PathVariable("year") Long year, 
			@PathVariable("month") Long month, @PathVariable("module") String module) {
		return invoiceResumeService.getInvoiceResumeRow(branchId, year, month, module);
	}
	
	/**
	 * API para ordenar los conteos de todas las empresas anualmente.
	 * @param year (Año de los conteos requerido).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los conteos anuales de cada sucursal.
	 */
	@GetMapping("/data-table/{year}/{module}")
	public List<String[]> orderResumesToTable(@PathVariable("year") Long year, @PathVariable("module") String module) {
		return invoiceResumeService.orderResumesToTable(year, module);
	}
	
}
