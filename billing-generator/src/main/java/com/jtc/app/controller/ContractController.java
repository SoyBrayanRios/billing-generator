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

@RestController
@CrossOrigin("*")
@RequestMapping("/api/contract")
public class ContractController {

	@Autowired
	private ContractService contractFeService;

	@PostMapping("/")
	public Contract saveContract(@RequestBody Contract contract) throws Exception{
		return contractFeService.saveContract(contract);
	}
	
	@GetMapping("/all")
	public List<Contract> getContracts() {
		return contractFeService.getContracts();
	}
	
	@GetMapping("/{contractId}")
	public Contract getContractById(@PathVariable(name = "contractId") String contractId) {
		return contractFeService.getContractById(contractId);
	}
	
	public Contract getContractByBranch(Long branchId, String module) {
		return contractFeService.getContractByBranch(branchId, module);
	}
	
	public void deleteContract(String contractId) {
		contractFeService.deleteContract(contractId);
	}
	
	public List<String> getSharedContracts(String module) {
		return contractFeService.getSharedContracts(module);
	}
	
}
