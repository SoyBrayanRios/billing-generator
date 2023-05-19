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

import com.jtc.app.primary.entity.Contract;
import com.jtc.app.service.ContractService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada contrato de FE, NE y DS.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/contract")
public class ContractController {

	@Autowired
	private ContractService contractFeService;

	/**
	 * API para guardar un contrato.
	 * @param contract (El contrato que se desea guardar).
	 * @return El objeto con los datos del contrato guardado en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public Contract saveContract(@RequestBody Contract contract) throws Exception{
		return contractFeService.saveContract(contract);
	}
	
	/**
	 * API para obtener todos los contratos de la base de datos.
	 * @return Lista con los objetos que representan a los contratos de la base de datos.
	 */
	@GetMapping("/all")
	public List<Contract> getContracts() {
		return contractFeService.getContracts();
	}
	
	/**
	 * API para obtener los datos de un contrato según su ID.
	 * @param contractId (ID del contrato a consultar).
	 * @return El objeto con los datos del contrato asociado al ID suministrado.
	 */
	@GetMapping("/{contractId}")
	public Contract getContractById(@PathVariable(name = "contractId") String contractId) {
		return contractFeService.getContractById(contractId);
	}
	
	/**
	 * API para obtener los datos de un contrato según el ID de la sucursal y el módulo indicado.
	 * @param branchId (ID de la sucursal).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return El objeto con los datos del contrato asociado al ID de sucursal suministrado.
	 */
	@GetMapping("/{branchId}/{module}")
	public Contract getContractByBranch(@PathVariable(name = "branchId") Long branchId, 
			@PathVariable(name = "module") String module) {
		return contractFeService.getContractByBranch(branchId, module);
	}
	
	/**
	 * API para obtener los datos de los contratos compartidos padre
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan a los contratos compartidos padre de la base de datos.
	 */
	@GetMapping("/{module}")
	public List<String> getSharedContracts(@PathVariable(name = "module") String module) {
		return contractFeService.getSharedContracts(module);
	}
	
}
