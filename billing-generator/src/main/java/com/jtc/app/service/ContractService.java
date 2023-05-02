package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Contract;

public interface ContractService {
	public Contract saveContract(Contract contract) throws Exception;
	public List<Contract> getContracts();
	public List<String> getSharedContracts(String module);
	public Contract getContractById(String contractId);
	public Contract getContractByBranch(Long branchId, String module);
	public void deleteContract(String contractId);
	public List<Contract> saveFeContractsFromFile(String filePath);
	public List<Contract> saveNeContractsFromFile(String filePath);
}
