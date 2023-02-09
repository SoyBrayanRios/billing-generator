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
	private ContractService contractService;

	@PostMapping("/")
	public Contract saveContract(@RequestBody Contract contract) throws Exception{
		return contractService.saveContract(contract);
	}
	
	@GetMapping("/all")
	public List<Contract> getContracts() {
		return contractService.getContracts();
	}
	
	@GetMapping("/{contractId}")
	public Contract getContractById(@PathVariable(name = "contractId") String contractId) {
		return contractService.getContractById(contractId);
	}
	
	public Contract getContractByBranch(Long branchId) {
		return contractService.getContractByBranch(branchId);
	}
	
	public void deleteContract(String contractId) {
		contractService.deleteContract(contractId);
	}
	
	public List<String> getSharedContracts() {
		return contractService.getSharedContracts();
	}
	
}
