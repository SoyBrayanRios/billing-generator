package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.ContractFE;

public interface ContractFERepository extends JpaRepository<ContractFE, String> {

	@Query(value = "select * from contract_fe where contract_id=:contractId", nativeQuery = true)
	public ContractFE getContractById(String contractId);
	
	@Query(value = "select * from contract_fe where branch_id=:branchId order by contract_date desc limit 1", nativeQuery = true)
	public ContractFE findByBranchId(Long branchId);
	
	@Query(value = "select distinct(shared_contract_id) from contract_fe where shared_contract_id notnull", nativeQuery = true)
	public List<String> getSharedContracts();
}
