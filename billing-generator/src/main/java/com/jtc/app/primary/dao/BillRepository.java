package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Bill;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "bill".
 *
 */
public interface BillRepository extends JpaRepository<Bill, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener todas las facturas generadas en un periodo dado.
	 * @param year (Año de la facturación requerida).
	 * @param month (Mes de la facturación requerida).
	 * @return Lista con los objetos que representan las facturas generadas en el mes y año correspondientes.
	 */
	@Query(value = "select * from bill where \"year\"=:year and \"month\"=:month order by bill_id asc", nativeQuery = true)
	public List<Bill> getFaceldiReport(Long year, Long month);
	
}
