package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.service.FrequencyService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/frequency")
public class FrequencyController {

	@Autowired
	FrequencyService frequencyService;
	
	@PostMapping("/")
	public Frequency saveFrequency(@RequestBody Frequency frequency) throws Exception {
		return frequencyService.saveFrequency(frequency);
	}

	@GetMapping("/all")
	public List<Frequency> getFrequencies() {
		return frequencyService.getFrequencies();
	}

	@GetMapping("/{frequencyId}")
	public Frequency getFrequencyById(@PathVariable(name = "frequencyId") Long frequencyId) {
		return frequencyService.getFrequencyById(frequencyId);
	}
	
}
