package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Contract;
import com.jtc.app.primary.entity.ContractCancellation;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "cancellation".
 *
 */
public interface CancellationRepository extends JpaRepository<ContractCancellation, Long> {
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cancelación asociada a un contrato.
	 * @param contractId (Número del contrato).
	 * @return El objeto con los datos de la cancelación asociada al número del contrato.
	 */
	@Query(value = "select * from cancellation where contract_id=:contractId", nativeQuery = true)
	public ContractCancellation findByContractId(String contractId);

}
