package com.jtc.app.primary.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.InvoiceResume;

@Transactional
public interface InvoiceResumeRepository extends JpaRepository<InvoiceResume, Long> {

	@Query(value = "select * from invoice_resume where branch_id=:branchId and \"year\"=:year and \"month\"=:month and module=:module", 
			nativeQuery = true)
	public InvoiceResume findByBranchYearMonth(Long branchId, Long year, Long month, String module);
	
	@Query(value = "select * from invoice_resume where \"year\"=:year and \"month\"=:month and module=:module", 
			nativeQuery = true)
	public List<InvoiceResume> findByYearMonthModule(Long year, Long month, String module);
	
	@Query(value = "select * from invoice_resume where module=:module", 
			nativeQuery = true)
	public List<InvoiceResume> findByModule(String module);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update invoice_resume set issued_invoices=:issuedInvoices where resume_id=:resumeId ;", nativeQuery = true)
	public void updateById(Long resumeId, Long issuedInvoices);
	
}
