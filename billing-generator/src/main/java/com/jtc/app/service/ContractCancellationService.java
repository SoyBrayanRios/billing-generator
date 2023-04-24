package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.ContractCancellation;

public interface ContractCancellationService {

	public ContractCancellation saveCancellation(ContractCancellation contractCancellation) throws Exception;
	public List<ContractCancellation> getCancellations();
	public void deleteCancellation(String cancellationId);
	
}
