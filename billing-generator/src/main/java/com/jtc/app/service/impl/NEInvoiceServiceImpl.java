package com.jtc.app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.secondary.dao.NEInvoiceRepository;
import com.jtc.app.secondary.entity.NEInvoice;
import com.jtc.app.service.NEInvoiceService;

@Service
public class NEInvoiceServiceImpl implements NEInvoiceService {

	@Autowired
	private NEInvoiceRepository neInvoiceRepository;
	
	/*@Override
	public List<NEInvoice> getInvoicesByStatus(int status) {
		return neInvoiceRepository.findAllByStatus(status);
	}*/

	@Override
	public NEInvoice geInvoice(String invoiceNumber, Long branchId) {
		return neInvoiceRepository.getSpecificInvoice(invoiceNumber, branchId);
	}

	@Override
	public List<NEInvoice> getIssuedInvoices() {
		return neInvoiceRepository.findAllIssuedInvoice();
	}

	@Override
	public Date getFirstIssuedDate(Long branchId) {
		return neInvoiceRepository.getFirstIssuedDate(branchId);
	}

	@Override
	public List<NEInvoice> getIssuedInvoicesByYearMonth(Long year, Long month) {
		return neInvoiceRepository.findIssuedInvoicesByYearMonth(year, month);
	}

}
