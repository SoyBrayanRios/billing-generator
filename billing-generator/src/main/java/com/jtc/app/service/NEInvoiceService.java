package com.jtc.app.service;

import java.util.Date;
import java.util.List;

import com.jtc.app.secondary.entity.FEInvoice;
import com.jtc.app.secondary.entity.NEInvoice;

/**
 * Esta interface define los servicios relacionados a la clase NEInvoice.
 *
 */
public interface NEInvoiceService {
	
	//public List<NEInvoice> getInvoicesByStatus(int status);
	public List<NEInvoice> getIssuedInvoices();
	public List<NEInvoice> getIssuedInvoicesByYearMonth(Long year, Long month);
	public NEInvoice geInvoice(String invoiceNumber, Long branchId);
	public Date getFirstIssuedDate(Long branchId);
	
}
