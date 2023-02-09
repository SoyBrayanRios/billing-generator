package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Contract;

public interface ContractService {
	public Contract saveContract(Contract contract) throws Exception;
	public List<Contract> getContracts();
	public List<String> getSharedContracts();
	public Contract getContractById(String contractId);
	public Contract getContractByBranch(Long branchId);
	public void deleteContract(String contractId);
	public List<Contract> saveContractsFromFile(String filePath);
}
