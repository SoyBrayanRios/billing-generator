package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.ContractNE;

public interface ContractNEService {
	public ContractNE saveContract(ContractNE contract) throws Exception;
	public List<ContractNE> getContracts();
	public List<String> getSharedContracts();
	public ContractNE getContractById(String contractId);
	public ContractNE getContractByBranch(Long branchId);
	public void deleteContract(String contractId);
	public List<ContractNE> saveContractsFromFile(String filePath);
}
