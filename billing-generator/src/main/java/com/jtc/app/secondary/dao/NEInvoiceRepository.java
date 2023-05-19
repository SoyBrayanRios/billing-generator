package com.jtc.app.secondary.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.NEInvoice;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "ne_documentos".
 *
 */
public interface NEInvoiceRepository extends JpaRepository<NEInvoice, Long> {

	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener una nómina específica.
	 * @param invoiceNumber (Número de la nómina).
	 * @param branchId (Número de la sucursal).
	 * @return El objeto con los datos de la nómina.
	 */
	@Query(value = "select * from ne_documentos where (raw\\:\\:json -> 'sucursal' ->> 'id')\\:\\:numeric =:branchId and "
			+ "(raw\\:\\:json ->> 'numeroDocumento') =:invoiceNumber ", nativeQuery = true)
	public NEInvoice getSpecificInvoice(String invoiceNumber, Long branchId);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todas las nóminas que se encuentran en estado emitido.
	 * @return Lista con los objetos que representan a las nóminas emitidas.
	 */
	@Query(value = "select * from ne_documentos", nativeQuery = true)
	public List<NEInvoice> findAllIssuedInvoice();
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todas las nóminas que se encuentran
	 * en estado emitido y que corresponden al periodo definido.
	 * @param year (Año al que corresponden las nóminas).
	 * @param month (Mes al que corresponden las nóminas).
	 * @return Lista con los objetos que representan a las nóminas emitidas en el periodo definido.
	 */
	@Query(value = "select * from ne_documentos where extract(month from substring(raw\\:\\:json ->> 'fechaEmision',1,10)\\:\\:date) =:month and "
			+ "extract(year from substring(raw\\:\\:json ->> 'fechaEmision',1,10)\\:\\:date) =:year", nativeQuery = true)
	public List<NEInvoice> findIssuedInvoicesByYearMonth(Long year, Long month);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener la fecha de primera emisión de nóminas
	 * de una sucursal.
	 * @param branchId (ID de la sucursal).
	 * @return La fecha de primera emisión de nóminas.
	 */
	@Query(value = "select min(substring(raw\\:\\:json ->> 'fechaEmision',1,10)\\:\\:date) "
			+ "from ne_documentos where id_sucursal_empleador =:branchId", nativeQuery = true)
	public Date getFirstIssuedDate(Long branchId);
	
}
