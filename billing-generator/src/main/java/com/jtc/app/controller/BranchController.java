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

import com.jtc.app.primary.entity.Branch;
import com.jtc.app.service.BranchService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada sucursal.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/branches")
public class BranchController {
	
	@Autowired
	private BranchService branchService;
	
	/**
	 * API para guardar una sucursal.
	 * @param branch (La sucursal que se desea guardar).
	 * @return El objeto con los datos de la sucursal guardada en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Branch saveBranch(@RequestBody Branch branch) throws Exception {
		return branchService.saveBranch(branch);
	}

	/**
	 * API para obtener todas las sucursales de la base de datos.
	 * @return Lista con los objetos que representan a las sucursales de la base de datos.
	 */
	@GetMapping("/all")
	public List<Branch> getBranches() {
		return branchService.getBranches();
	}

	/**
	 * API para obtener los datos de una sucursal según su ID.
	 * @param branchId (ID de la sucursal)
	 * @return El objeto con los datos de la sucursal que corresponda al ID suministrado.
	 */
	@GetMapping("/{branchId}")
	public Branch getBranchById(@PathVariable("branchId") Long branchId) {
		return branchService.getBranchById(branchId);
	}
	
	/**
	 * API para obtener todas las sucursales de la base de datos. 
	 * Particularmente, esta API consulta la base de datos de producción de Faceldi para identificar las sucursales nuevas 
	 * y posteriormente guardar dicha información en la base de datos de la aplicación. 
	 * @return Lista con los objetos que representan a las sucursales de la base de datos.
	 */
	@GetMapping("/u")
	public List<Branch> updateAllBranches() {
		return branchService.updateAllBranches();
	}
	
	/**
	 * API para obtener todas las sucursales de la base de datos que no tienen un contrato asociado.
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan a las sucursales sin contrato de la base de datos.
	 */
	@GetMapping("/to-update/{module}")
	public List<Branch> getBranchesWithoutContract(@PathVariable("module") String module) {
		return branchService.getBranchesWithoutContract(module);
	}
}
