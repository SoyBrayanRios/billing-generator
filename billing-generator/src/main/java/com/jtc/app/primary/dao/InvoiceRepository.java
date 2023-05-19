package com.jtc.app.primary.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Invoice;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "invoice".
 *
 */
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener la información de una factura proveniente de la base de datos.
	 * de producción de Faceldi.
	 * @param branchId (ID de la sucursal).
	 * @param invoiceNumber (Número de la factura).
	 * @param issuedDate (fecha de emisión de la factura).
	 * @return El objeto con los datos de la factura que corresponda a los parámetros dados.
	 */
	@Query(value = "select * from invoice where branch_id=:branchId and invoice_number=:invoiceNumber and issued_date=:issuedDate",
			nativeQuery = true)
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Date issuedDate);
	
	/**
	 * Query que permite actualizar la columna "counted" de una factura de la base de datos. 
	 * @param invoiceId (ID de la factura).
	 */
	@Modifying(clearAutomatically = true)
	@Query(value = "update invoice set counted=true where invoice_id=:invoiceId ;", nativeQuery = true)
	public void updateById(Long invoiceId);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de facturas emitidas de FE emitidas en un periodo determinado.
	 * @param branchId (ID de la sucursal).
	 * @param startDate (Fecha inicial del periodo que desea consultar).
	 * @param limitDate (Fecha final del periodo que desea consultar).
	 * @return El número total de facturas emitidas en el periodo dado.
	 */
	@Query(value = "select count(invoice_id) from invoice where branch_id =:branchId and issued_date between "
			+ ":startDate and :limitDate and document_type in ('FV', 'NC', 'FE', 'FCD', 'FCF', 'ND');", nativeQuery = true)
	public Long getFeIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de facturas emitidas de DS emitidas en un periodo determinado.
	 * @param branchId (ID de la sucursal).
	 * @param startDate (Fecha inicial del periodo que desea consultar).
	 * @param limitDate (Fecha final del periodo que desea consultar).
	 * @return El número total de facturas emitidas en el periodo dado.
	 */
	@Query(value = "select count(invoice_id) from invoice where branch_id =:branchId and issued_date between "
			+ ":startDate and :limitDate and document_type in ('DS', 'NAS');", nativeQuery = true)
	public Long getDsIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener la cantidad de facturas emitidas de NE emitidas en un periodo determinado.
	 * @param branchId (ID de la sucursal).
	 * @param startDate (Fecha inicial del periodo que desea consultar).
	 * @param limitDate (Fecha final del periodo que desea consultar).
	 * @return El número total de facturas emitidas en el periodo dado.
	 */
	@Query(value = "select count(invoice_id) from invoice where branch_id =:branchId and issued_date between "
			+ ":startDate and :limitDate and document_type in ('NI', 'NIA');", nativeQuery = true)
	public Long getNeIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener las facturas no contadas de la base de datos.
	 * @return Lista con los objetos que representan las facturas que aún no se han contado y no se han sumado en la tabla "invoice_resume"
	 */
	@Query(value = "select * from invoice where not counted;", nativeQuery = true)
	public List<Invoice> getInvoicesToCount();
	
}
