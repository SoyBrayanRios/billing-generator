package com.jtc.app.service;

import java.util.Date;
import java.util.List;

import com.jtc.app.secondary.entity.FEInvoice;

public interface FEInvoiceService {
	
	public List<FEInvoice> getInvoicesByStatus(int status);
	public List<FEInvoice> getIssuedInvoices();
	public List<FEInvoice> getIssuedInvoicesByYearMonth(Long year, Long month);
	public FEInvoice geInvoice(String invoiceNumber, Long branchId);
	public Date getFirstIssuedDate(Long branchId);
	
}
