package com.jtc.app.service;

import java.util.Date;
import java.util.List;

import com.jtc.app.primary.entity.Invoice;

/**
 * Esta interface define los servicios relacionados a la clase Invoice.
 *
 */
public interface InvoiceService {
	
	public Invoice saveInvoice(Invoice invoice) throws Exception;
	public List<Invoice> getInvoices();
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Date issuedDate);
	public void deleteInvoice(Long invoiceId);
	public void updateById(Long invoiceId) throws Exception;
	public Long getFeIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	public Long getDsIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	public Long getNeIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate);
	public List<Invoice> getInvoicesToCount();
	public List<Invoice> updateAllInvoices();
	public List<Invoice> updateInvoicesByYearMonth(Long year, Long month);
	public Long getIssuedInvoicesDuringContract(Long branchId, String startDate, String limitDate, String module);
}
