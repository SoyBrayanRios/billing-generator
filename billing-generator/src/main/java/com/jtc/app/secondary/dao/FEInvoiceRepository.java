package com.jtc.app.secondary.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEInvoice;

public interface FEInvoiceRepository extends JpaRepository<FEInvoice, Long> {

	public List<FEInvoice> findAllByStatus(int status);
	
	@Query(value = "select * from documentos where estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17) and "
			+ "numero_documento=:invoiceNumber and id_sucursal_emisor=:branchId;", nativeQuery = true)
	public FEInvoice getSpecificInvoice(String invoiceNumber, Long branchId);
	
	@Query(value = "select * from documentos where tipo_documento= 'FV' and estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17)", nativeQuery = true)
	public List<FEInvoice> findAllIssuedInvoice();
	
	@Query(value = "select * from documentos where tipo_documento= 'FV' and estado in (3, 5, 6, 7, 8, 11, 12, 13, 14, 15, 17)\n"
			+ "and extract(year from fecha_hora_emision)=:year and\n"
			+ "extract (month from fecha_hora_emision)=:month ;", nativeQuery = true)
	public List<FEInvoice> findIssuedInvoicesByYearMonth(Long year, Long month);
	
	@Query(value = "select min(fecha_hora_emision) from documentos where id_sucursal_emisor=:branchId "
			+ "and tipo_documento in ('FV','FE','FCF','FCD') and prefijo_factura != 'SETT';", nativeQuery = true)
	public Date getFirstIssuedDate(Long branchId);
	
}
