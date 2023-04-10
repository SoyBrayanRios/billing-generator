package com.jtc.app.secondary.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.NEInvoice;

public interface NEInvoiceRepository extends JpaRepository<NEInvoice, Long> {

	//public List<NEInvoice> findAllByStatus(int status);
	
	@Query(value = "select * from ne_documentos where raw::json -> 'sucursal'=:branchId and "
			+ "raw::json -> 'numeroDocumento'=:invoiceNumber ", nativeQuery = true)
	public NEInvoice getSpecificInvoice(String invoiceNumber, Long branchId);
	
	@Query(value = "select * from ne_documentos", nativeQuery = true)
	public List<NEInvoice> findAllIssuedInvoice();
	
	@Query(value = "select * from ne_documentos where extract(month from substring(raw::json ->> 'fechaEmision',1,10)::date) =:month and "
			+ "extract(year from substring(raw::json ->> 'fechaEmision',1,10)::date) =:year", nativeQuery = true)
	public List<NEInvoice> findIssuedInvoicesByYearMonth(Long year, Long month);
	
	@Query(value = "select min(substring(raw::json ->> 'fechaEmision',1,10)::date) "
			+ "from ne_documentos where id_sucursal_emisor=:branchId;", nativeQuery = true)
	public Date getFirstIssuedDate(Long branchId);
	
}
