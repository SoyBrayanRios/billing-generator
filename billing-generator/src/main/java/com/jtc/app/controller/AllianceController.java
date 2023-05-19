package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.service.AllianceService;

/**
 * Controlador que expone todas las API que interactuan con la información de la alianzas.
 *
 */
@RestController
@RequestMapping("/api/alliance")
public class AllianceController {

	@Autowired
	private AllianceService allianceService;
	
	/**
	 * API para guardar una alianza.
	 * @param alliance (La alianza que se desea guardar).
	 * @return El objeto con los datos de la alianza guardada en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Alliance saveAlliance(@RequestBody Alliance alliance) throws Exception{
		return allianceService.saveAlliance(alliance);
	}
	
	/**
	 * API para obtener todas las alianzas de la base de datos.
	 * @return Lista de objetos de tipo Alliance.
	 */
	@GetMapping("/")
	public List<Alliance> getAlliances() {
		return allianceService.getAlliances();
	}
	
	
	/**
	 * API para obtener los datos de una alianza según su nombre.
	 * @param name (Nombre de la alianza).
	 * @return El objeto con los datos de la alianza que corresponda al nombre suministrado.
	 * @throws Exception
	 */
	@GetMapping("/{name}")
	public Alliance getAlliance(@PathVariable("name") String name) throws Exception {
		return allianceService.getAllianceByName(name);
	}
	
	/**
	 * API para obtener los datos de una alianza según su ID.
	 * @param allianceId (ID de la alianza).
	 * @return El objeto con los datos de la alianza que corresponda al ID suministrado.
	 * @throws Exception
	 */
	@GetMapping("/{allianceId}")
	public Alliance getAlliance(@PathVariable("id") Long allianceId) throws Exception {
		return allianceService.getAllianceById(allianceId);
	}
	
}
