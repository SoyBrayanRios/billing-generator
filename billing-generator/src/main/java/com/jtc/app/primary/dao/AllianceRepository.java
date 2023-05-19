package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Alliance;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "alliance".
 *
 */
public interface AllianceRepository extends JpaRepository<Alliance, Long> {
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener un alianza según su ID.
	 * @param allianceId (ID de la alianza).
	 * @return El objeto con los datos de la alianza que corresponda al ID suministrado.
	 */
	@Query(value = "select * from alliance where alliance_id=:allianceId", nativeQuery = true)
	public Alliance findByAllianceId(Long allianceId);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener un alianza según su nombre. 
	 * @param allianceName (Nombre de la alianza).
	 * @return El objeto con los datos de la alianza que corresponda al nombre suministrado.
	 */
	public Alliance findByName(String allianceName);

}
