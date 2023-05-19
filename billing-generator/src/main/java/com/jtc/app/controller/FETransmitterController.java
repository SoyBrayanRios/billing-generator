package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.Transmitter;
import com.jtc.app.service.FETransmitterService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada emisor 
 * directamente en la base de datos de Faceldi.
 *
 */
@RestController
@RequestMapping("/api/emisores_fe")
public class FETransmitterController {

	@Autowired
	private FETransmitterService feTransmitterService; 
	
	/**
	 * API para consultar todas las sucursales que actualmente han registrado al menos un documento en
	 * la base de datos de producción en Faceldi.
	 * @return Lista con los objetos que representan a las sucursales de la base de datos de producción.
	 */
	@GetMapping("/active")
	public List<Transmitter> getFEActiveTransmitters() {
		return feTransmitterService.getFEActiveTransmitters();
	}
	
}
