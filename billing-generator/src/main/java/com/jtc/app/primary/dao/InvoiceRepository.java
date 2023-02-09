package com.jtc.app.primary.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Invoice;

@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	@Query(value = "select * from invoice where branch_id=:branchId and invoice_number=:invoiceNumber and "
			+ "buyer_id=:buyerId and issued_date=:issuedDate ;", nativeQuery = true)
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Long buyerId, Date issuedDate);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update invoice set counted=true where invoice_id=:invoiceId ;", nativeQuery = true)
	public void updateById(Long invoiceId);
	
	@Query(value = "select count(invoice_id) from invoice where branch_id =:branchId and issued_date between "
			+ ":startDate and :limitDate ;", nativeQuery = true)
	public Long getIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	
	@Query(value = "select * from invoice where not counted;", nativeQuery = true)
	public List<Invoice> getInvoicesToCount();
	
}
