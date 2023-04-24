package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Contract;
import com.jtc.app.primary.entity.ContractCancellation;

public interface CancellationRepository extends JpaRepository<ContractCancellation, Long> {
	
	@Query(value = "select * from cancellation where contract_id=:contractId", nativeQuery = true)
	public ContractCancellation findByContractId(String contractId);

}
