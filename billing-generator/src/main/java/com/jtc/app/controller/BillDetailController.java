package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.BillDetail;
import com.jtc.app.service.BillDetailService;

/**
 * Controlador que expone todas las API que interactuan con la información general de las ejecuciones de facturación 
 * que se han generado en Kosmos y que se guardaron en la base de datos.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/bill-detail")
public class BillDetailController {

	@Autowired
	private BillDetailService billDetailService;
	
	/**
	 * API para consultar la base de datos y obtener el listado de ejecuciones de facturación que se han guardado en la base de 
	 * datos de la aplicación.
	 * @return Lista con los datos generales de las ejecuciones de facturación alojadas en la base de datos
	 */
	@GetMapping("/list")
	public List<BillDetail> getBillDetailList() {
		return billDetailService.getBillDetailList();
	}
	
}
