package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Client;
import com.jtc.app.service.ClientService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada cliente.
 *
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	/**
	 * API para guardar un cliente.
	 * @param client (El cliente que se desea guardar).
	 * @return El objeto con los datos del cliente guardado en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Client saveClient(@RequestBody Client client) throws Exception {
		return clientService.saveClient(client);
	}

	/**
	 * API para obtener todos los clientes de la base de datos.
	 * @return Lista con los objetos que representan a los clientes de la base de datos.
	 */
	@GetMapping("/all")
	public List<Client> getClients() {
		return clientService.getClients();
	}
	
	/**
	 * API para obtener los datos de un cliente según su NIT.
	 * @param clientId (Nit de la empresa a consultar).
	 * @return El objeto con los datos del cliente asociado al NIT suministrado.
	 */
	@GetMapping("/{clientId}")
	public Client getClientByNit(@PathVariable String clientId) {
		return clientService.getClientByNit(clientId);
	}
	
	/**
	 * API para obtener los clientes nuevos de la base de datos de producción en Faceldi e insertarlos 
	 * en la base de datos de Kosmos.
	 * @return Lista con los objetos que representan a los clientes de la base de datos.
	 */
	@GetMapping("/u")
	public List<Client> updateAllClients() {
		return clientService.updateAllClients();
	}
	
}
