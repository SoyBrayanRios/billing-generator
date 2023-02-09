package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Client;
import com.jtc.app.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@PostMapping("/")
	public Client saveClient(@RequestBody Client client) throws Exception {
		return clientService.saveClient(client);
	}

	@GetMapping("/all")
	public List<Client> getClients() {
		return clientService.getClients();
	}
	

	@GetMapping("/{clientId}")
	public Client getClientByNit(@PathVariable String clientId) {
		return clientService.getClientByNit(clientId);
	}

	@DeleteMapping("/{clientId}")
	public void deleteClient(@PathVariable String clientId) {
		clientService.deleteClient(clientId);
	}

	@GetMapping("/u")
	public List<Client> updateAllClients() {
		return clientService.updateAllClients();
	}
}
