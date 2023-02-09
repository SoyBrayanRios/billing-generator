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

import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.service.AllianceService;

@RestController
@RequestMapping("/api/alliance")
public class AllianceController {

	@Autowired
	private AllianceService allianceService;
	
	@GetMapping("/")
	public List<Alliance> getAlliances() {
		return allianceService.getAlliances();
	}
	
	@PostMapping("/")
	public Alliance saveAlliance(@RequestBody Alliance alliance) throws Exception{
		return allianceService.saveAlliance(alliance);
	}
	
	@GetMapping("/{name}")
	public Alliance getAlliance(@PathVariable("name") String name) throws Exception {
		return allianceService.getAllianceByName(name);
	}
	
	@GetMapping("/{allianceId}")
	public Alliance getAlliance(@PathVariable("id") Long allianceId) throws Exception {
		return allianceService.getAllianceById(allianceId);
	}
	
	@DeleteMapping("/{allianceId}")
	public void deleteAlliance(@PathVariable("allianceId") Long allianceId) throws Exception {
		allianceService.deleteAlliance(allianceId);
	}
	
}
