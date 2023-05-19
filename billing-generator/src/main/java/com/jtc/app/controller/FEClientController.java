package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.service.FEClientService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada cliente 
 * directamente en la base de datos de Faceldi.
 *
 */
@RestController
@RequestMapping("/api/clients_fe")
public class FEClientController {

	@Autowired
	private FEClientService feClientService;
	
	/**
	 * API para obtener todos los clientes directamente de la base de datos de producción de Faceldi.
	 * @return Lista con los objetos que representan a los clientes de la base de datos.
	 */
	@GetMapping("/all")
	public List<FEClient> getFEActiveClients() {
		return feClientService.getFEActiveClients(); 
	}
}
