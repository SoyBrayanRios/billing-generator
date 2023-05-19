package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Branch;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "branch".
 *
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener una sucursal según su ID.
	 * @param branchId (ID de la sucursal).
	 * @return El objeto con los datos de la sucursal que corresponda al ID suministrado.
	 */
	@Query(value = "select * from branch where branch_id=:branchId", nativeQuery = true)
	public Branch findByBranchId(Long branchId);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener una sucursal según su NIT.
	 * @param clientId (NIT del cliente al que pertenece la sucursal).
	 * @return El objeto con los datos de la sucursal que corresponda al NIT suministrado.
	 */
	@Query(value = "select * from branch where client_id=:clientId", nativeQuery = true)
	public Branch findByClientId(String clientId);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener las sucursales de FE sin contrato asociado.
	 * @return Lista con los objetos que representan las sucursales sin contrato de FE asociado.
	 */
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join (select * from contract where \"module\" = 'FE') co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and b.fe order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractFe();
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener las sucursales de NE sin contrato asociado.
	 * @return Lista con los objetos que representan las sucursales sin contrato de NE asociado.
	 */
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join (select * from contract where \"module\" = 'NE') co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and b.ne order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractNe();
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener las sucursales de DS sin contrato asociado.
	 * @return Lista con los objetos que representan las sucursales sin contrato de DS asociado.
	 */
	@Query(value = "select b.branch_id, b.\"active\", b.codigo, b.branch_name, b.country, b.department, b.department_name, "
			+ "b.state, b.state_name, b.center, b.address, b.address_complement, b.phone, b.email, b.client_id, b.ds, b.fe, b.ne "
			+ "from branch b\n"
			+ "left join (select * from contract where \"module\" = 'DS') co on co.branch_id = b.branch_id\n"
			+ "where co.branch_id isnull and b.active and b.ds order by b.branch_id asc", nativeQuery = true)
	public List<Branch> findBranchesWithoutContractDs();
	
}
