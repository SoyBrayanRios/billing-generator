package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.service.FEClientService;

@RestController
@RequestMapping("/api/clients_fe")
public class FEClientController {

	@Autowired
	private FEClientService feClientService;
	
	@GetMapping("/all")
	public List<FEClient> getFEActiveClients() {
		return feClientService.getFEActiveClients(); 
	}
}
