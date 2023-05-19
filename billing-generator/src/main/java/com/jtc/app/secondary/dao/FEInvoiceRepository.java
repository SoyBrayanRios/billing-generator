package com.jtc.app.secondary.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEInvoice;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "documentos".
 *
 */
public interface FEInvoiceRepository extends JpaRepository<FEInvoice, Long> {

	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todas las facturas de FE y NE
	 * que tengan el estado suministrado
	 * @param status (Estado de la factura Ejm: 2, 5, etc....)
	 * @return Lista con los objetos que representan a las facturas de FE y DS.
	 */
	public List<FEInvoice> findAllByStatus(int status);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener una factura específica.
	 * @param invoiceNumber (Número de la factura).
	 * @param branchId (Número de la sucursal).
	 * @return El objeto con los datos de la factura.
	 */
	@Query(value = "select * from documentos where estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17) and "
			+ "numero_documento=:invoiceNumber and id_sucursal_emisor=:branchId;", nativeQuery = true)
	public FEInvoice getSpecificInvoice(String invoiceNumber, Long branchId);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todas las facturas de FE y DS que se encuentran en estado emitido.
	 * @return Lista con los objetos que representan a las facturas de FE y DS emitidas.
	 */
	@Query(value = "select * from documentos where tipo_documento= 'FV' and estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17)", nativeQuery = true)
	public List<FEInvoice> findAllIssuedInvoice();
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todas las facturas de FE y NE que se encuentran
	 * en estado emitido y que corresponden al periodo definido.
	 * @param year (Año al que corresponden las facturas).
	 * @param month (Mes al que corresponden las facturas).
	 * @return Lista con los objetos que representan a las facturas de FE y DS emitidas en el periodo definido.
	 */
	@Query(value = "select * from documentos where tipo_documento= 'FV' and estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17)\n"
			+ "and extract(year from fecha_hora_emision)=:year and\n"
			+ "extract (month from fecha_hora_emision)=:month ;", nativeQuery = true)
	public List<FEInvoice> findIssuedInvoicesByYearMonth(Long year, Long month);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener la fecha de primera emisión de facturas
	 * de una sucursal.
	 * @param branchId (ID de la sucursal).
	 * @return La fecha de primera emisión de facturas.
	 */
	@Query(value = "select min(fecha_hora_emision) from documentos where id_sucursal_emisor=:branchId "
			+ "and tipo_documento in ('FV','FE','FCF','FCD') and prefijo_factura != 'SETT';", nativeQuery = true)
	public Date getFirstIssuedDate(Long branchId);
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener la fecha de primera emisión de documentos soporte
	 * de una sucursal.
	 * @param branchId (ID de la sucursal).
	 * @return La fecha de primera emisión de documento soporte.
	 */
	@Query(value = "select min(fecha_hora_emision) from documentos where id_sucursal_emisor=:branchId "
			+ "and tipo_documento in ('DS') and prefijo_factura != 'SEDS';", nativeQuery = true)
	public Date getFirstIssuedDateDs(Long branchId);
	
}
