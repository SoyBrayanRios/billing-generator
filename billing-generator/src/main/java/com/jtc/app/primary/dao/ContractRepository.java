package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Contract;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "contract".
 *
 */
public interface ContractRepository extends JpaRepository<Contract, String> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener los datos de un contrato según su ID.
	 * @param contractId (Número del contrato).
	 * @return El objeto con los datos del contrato que corresponda al ID suministrado.
	 */
	@Query(value = "select * from contract where contract_id=:contractId", nativeQuery = true)
	public Contract getContractById(String contractId);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener los datos de un contrato según el ID de la sucursal.
	 * @param branchId (ID de la sucursal).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return El objeto con los datos del contrato que corresponda al ID de sucursal suministrado.
	 */
	@Query(value = "select * from contract where branch_id=:branchId and module=:module "
			+ "order by contract_date desc limit 1", nativeQuery = true)
	public Contract findByBranchId(Long branchId, String module);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener los ID de los contratos compartidos.
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los números de contrato compartidos principales.
	 */
	@Query(value = "select distinct(shared_contract_id) from contract where shared_contract_id notnull and module =:module", nativeQuery = true)
	public List<String> getSharedContracts(String module);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la información de todos los contratos de la base de datos.
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan los contratos registrados en la base de datos.
	 */
	@Query(value = "select * from contract where module =:module", nativeQuery = true)
	public List<Contract> getContracts(String module);
}
