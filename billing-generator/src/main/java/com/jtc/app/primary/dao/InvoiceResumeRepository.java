package com.jtc.app.primary.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.InvoiceResume;

@Transactional
public interface InvoiceResumeRepository extends JpaRepository<InvoiceResume, Long> {

	@Query(value = "select * from invoice_resume where branch_id=:branchId and \"year\"=:year and \"month\"=:month ;", 
			nativeQuery = true)
	public InvoiceResume findByBranchYearMonth(Long branchId, Long year, Long month);
	
	@Query(value = "select * from invoice_resume where \"year\"=:year and \"month\"=:month ;", 
			nativeQuery = true)
	public List<InvoiceResume> findByYearMonth(Long year, Long month);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update invoice_resume set issued_invoices=:issuedInvoices where resume_id=:resumeId ;", nativeQuery = true)
	public void updateById(Long resumeId, Long issuedInvoices);
	
}
