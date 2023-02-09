package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, String> {

	@Query(value = "select * from contract where contract_id=:contractId", nativeQuery = true)
	public Contract getContractById(String contractId);
	
	@Query(value = "select * from contract where branch_id=:branchId order by contract_date desc limit 1", nativeQuery = true)
	public Contract findByBranchId(Long branchId);
	
	@Query(value = "select distinct(shared_contract_id) from contract where shared_contract_id notnull", nativeQuery = true)
	public List<String> getSharedContracts();
}
