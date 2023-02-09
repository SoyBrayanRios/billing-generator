package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.InvoiceResume;

public interface InvoiceResumeService {

	public InvoiceResume saveInvoiceResume(InvoiceResume invoiceResume) throws Exception;
	public InvoiceResume getInvoiceResumeRow(Long branchId, Long year, Long month);
	public List<InvoiceResume> getInvoiceResumeRowByYearMonth(Long year, Long month);
	public void updateById(Long resumeId, Long issuedInvoices) throws Exception;
	public List<InvoiceResume> getAllResumes();
	public List<String[]> orderResumesToTable(Long year);
	
}
