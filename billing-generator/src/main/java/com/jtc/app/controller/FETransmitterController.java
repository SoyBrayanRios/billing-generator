package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.FETransmitter;
import com.jtc.app.service.FETransmitterService;

@RestController
@RequestMapping("/api/emisores_fe")
public class FETransmitterController {

	@Autowired
	private FETransmitterService feTransmitterService; 
	
	@GetMapping("/active")
	public List<FETransmitter> getFEActiveTransmitters() {
		return feTransmitterService.getFEActiveTransmitters();
	}
	
}
