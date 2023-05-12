package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query(value = "select * from branch where branch_id=:branchId", nativeQuery = true)
	public Branch findByBranchId(Long branchId);
	
	@Query(value = "select * from branch where client_id=:clientId", nativeQuery = true)
	public Branch findByClientId(String clientId);
	
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join contract co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and fe order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractFe();
	
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join contract co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and ne order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractNe();
	
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join contract co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and ds order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractDs();
	
}
