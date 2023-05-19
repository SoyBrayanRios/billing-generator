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

/**
 * Controlador que expone todas las API que interactuan con la información de los tipos de frecuecia 
 * que puede manejar la aplicación.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/frequency")
public class FrequencyController {

	@Autowired
	private FrequencyService frequencyService;
	
	/**
	 * API para guardar un tipo de frecuencia.
	 * @param frequency (El tipo de frecuencia que desea guardar)
	 * @return El objeto con los datos detipo de frecuencia guardada en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Frequency saveFrequency(@RequestBody Frequency frequency) throws Exception {
		return frequencyService.saveFrequency(frequency);
	}

	/**
	 * API para obtener todas los tipos de frecuencia de la base de datos.
	 * @return Lista con los objetos que representan a los tipos de frecuencia de la base de datos.
	 */
	@GetMapping("/all")
	public List<Frequency> getFrequencies() {
		return frequencyService.getFrequencies();
	}

	/**
	 * API para obtener los datos de un tipo de frecuencia según su ID.
	 * @param frequencyId (ID del tipo de frecuencia).
	 * @return El objeto con los datos de la frecuencia que corresponda al ID suministrado.
	 */
	@GetMapping("/{frequencyId}")
	public Frequency getFrequencyById(@PathVariable(name = "frequencyId") Long frequencyId) {
		return frequencyService.getFrequencyById(frequencyId);
	}
	
}
