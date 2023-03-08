package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.ContractFE;

public interface ContractFEService {
	public ContractFE saveContract(ContractFE contract) throws Exception;
	public List<ContractFE> getContracts();
	public List<String> getSharedContracts();
	public ContractFE getContractById(String contractId);
	public ContractFE getContractByBranch(Long branchId);
	public void deleteContract(String contractId);
	public List<ContractFE> saveContractsFromFile(String filePath);
}
