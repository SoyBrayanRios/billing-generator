package com.jtc.app.service;

import java.util.Date;
import java.util.List;

import com.jtc.app.primary.entity.Invoice;

public interface InvoiceService {
	
	public Invoice saveInvoice(Invoice invoice) throws Exception;
	public List<Invoice> getInvoices();
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Long buyerId, Date issuedDate);
	public void deleteInvoice(Long invoiceId);
	public void updateById(Long invoiceId) throws Exception;
	public Long getIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	public List<Invoice> getInvoicesToCount();
	public List<Invoice> updateAllInvoices();
	public List<Invoice> updateInvoicesByYearMonth(Long year, Long month);
	public Long getIssuedInvoicesDuringContract(Long branchId, String startDate, String limitDate);
}