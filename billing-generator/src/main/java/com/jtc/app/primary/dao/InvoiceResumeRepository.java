package com.jtc.app.primary.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.InvoiceResume;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "invoice_resume".
 *
 */
@Transactional
public interface InvoiceResumeRepository extends JpaRepository<InvoiceResume, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de documentos emitidos por una sucursal en un periodo dado.
	 * @param branchId (ID de la sucursal).
	 * @param year (Año del periodo que se desea consultar).
	 * @param month (Mes del periodo que se desea consultar).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan el conteo de facturas asociados a la sucursal y el periodo dado.
	 */
	@Query(value = "select * from invoice_resume where branch_id=:branchId and \"year\"=:year and \"month\"=:month and module=:module", 
			nativeQuery = true)
	public InvoiceResume findByBranchYearMonth(Long branchId, Long year, Long month, String module);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de documentos emitidos por todas las sucursales en un periodo dado.
	 * @param year (Año del periodo que se desea consultar).
	 * @param month (Mes del periodo que se desea consultar).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan los conteos de facturas de todas las sucursales de la base de datos.
	 */
	@Query(value = "select * from invoice_resume where \"year\"=:year and \"month\"=:month and module=:module", 
			nativeQuery = true)
	public List<InvoiceResume> findByYearMonthModule(Long year, Long month, String module);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de documentos emitidos por todas las sucursales en un periodo dado
	 * se acuerdo con el servicio al que pertenen.
	 * @param module (Acrónimo que indica cual servicio pertenecen, bien sea FE, NE o DS).
	 * @return Lista con los objetos que representan los conteos de facturas de todas las sucursales de la base de datos asociadas al módulo indicado.
	 */
	@Query(value = "select * from invoice_resume where module=:module", 
			nativeQuery = true)
	public List<InvoiceResume> findByModule(String module);
	
	/**
	 * Query que permite actualizar la columna "issued_invoices" de una conteo de facturas de la base de datos.
	 * @param resumeId (ID del conteo de facturas).
	 * @param issuedInvoices (Cantidad de facturas emitidas).
	 */
	@Modifying(clearAutomatically = true)
	@Query(value = "update invoice_resume set issued_invoices=:issuedInvoices where resume_id=:resumeId ;", nativeQuery = true)
	public void updateById(Long resumeId, Long issuedInvoices);
	
}
