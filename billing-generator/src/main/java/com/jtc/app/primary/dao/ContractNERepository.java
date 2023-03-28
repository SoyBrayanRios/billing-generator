package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.ContractFE;
import com.jtc.app.primary.entity.ContractNE;

public interface ContractNERepository extends JpaRepository<ContractNE, String> {

	@Query(value = "select * from contract_ne where contract_id=:contractId", nativeQuery = true)
	public ContractNE getContractById(String contractId);
	
	@Query(value = "select * from contract_ne where branch_id=:branchId order by contract_date desc limit 1", nativeQuery = true)
	public ContractNE findByBranchId(Long branchId);
	
	@Query(value = "select distinct(shared_contract_id) from contract_ne where shared_contract_id notnull", nativeQuery = true)
	public List<String> getSharedContracts();
}
