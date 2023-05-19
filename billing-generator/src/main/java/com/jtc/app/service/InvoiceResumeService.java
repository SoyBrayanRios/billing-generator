package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.InvoiceResume;

/**
 * Esta interface define los servicios relacionados a la clase InvoiceResume.
 *
 */
public interface InvoiceResumeService {

	public InvoiceResume saveInvoiceResume(InvoiceResume invoiceResume) throws Exception;
	public InvoiceResume getInvoiceResumeRow(Long branchId, Long year, Long month, String module);
	public List<InvoiceResume> getInvoiceResumeRowByYearMonth(Long year, Long month, String module);
	public void updateById(Long resumeId, Long issuedInvoices) throws Exception;
	public List<InvoiceResume> getAllResumes(String module);
	public List<String[]> orderResumesToTable(Long year, String module);
	
}
