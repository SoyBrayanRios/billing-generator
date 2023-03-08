package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.ContractDS;

public interface ContractDSService {
	public ContractDS saveContract(ContractDS contract) throws Exception;
	public List<ContractDS> getContracts();
	public List<String> getSharedContracts();
	public ContractDS getContractById(String contractId);
	public ContractDS getContractByBranch(Long branchId);
	public void deleteContract(String contractId);
	public List<ContractDS> saveContractsFromFile(String filePath);
}
