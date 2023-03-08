package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.ContractDS;

public interface ContractDSRepository extends JpaRepository<ContractDS, String> {

	@Query(value = "select * from contract_ds where contract_id=:contractId", nativeQuery = true)
	public ContractDS getContractById(String contractId);
	
	@Query(value = "select * from contract_ds where branch_id=:branchId order by contract_date desc limit 1", nativeQuery = true)
	public ContractDS findByBranchId(Long branchId);
	
	@Query(value = "select distinct(shared_contract_id) from contract_ds where shared_contract_id notnull", nativeQuery = true)
	public List<String> getSharedContracts();
}
