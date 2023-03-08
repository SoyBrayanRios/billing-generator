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

import com.jtc.app.primary.entity.ContractFE;
import com.jtc.app.service.ContractFEService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/contract")
public class ContractFEController {

	@Autowired
	private ContractFEService contractFeService;

	@PostMapping("/")
	public ContractFE saveContract(@RequestBody ContractFE contract) throws Exception{
		return contractFeService.saveContract(contract);
	}
	
	@GetMapping("/all")
	public List<ContractFE> getContracts() {
		return contractFeService.getContracts();
	}
	
	@GetMapping("/{contractId}")
	public ContractFE getContractById(@PathVariable(name = "contractId") String contractId) {
		return contractFeService.getContractById(contractId);
	}
	
	public ContractFE getContractByBranch(Long branchId) {
		return contractFeService.getContractByBranch(branchId);
	}
	
	public void deleteContract(String contractId) {
		contractFeService.deleteContract(contractId);
	}
	
	public List<String> getSharedContracts() {
		return contractFeService.getSharedContracts();
	}
	
}
