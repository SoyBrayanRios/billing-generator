package com.jtc.app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.secondary.dao.FEInvoiceRepository;
import com.jtc.app.secondary.entity.FEInvoice;
import com.jtc.app.service.FEInvoiceService;

@Service
public class FEInvoiceServiceImpl implements FEInvoiceService{

	@Autowired
	private FEInvoiceRepository feInvoiceRepository;
	
	@Override
	public List<FEInvoice> getInvoicesByStatus(int status) {
		return feInvoiceRepository.findAllByStatus(status);
	}

	@Override
	public FEInvoice geInvoice(String invoiceNumber, Long branchId) {
		return feInvoiceRepository.getSpecificInvoice(invoiceNumber, branchId);
	}

	@Override
	public List<FEInvoice> getIssuedInvoices() {
		return feInvoiceRepository.findAllIssuedInvoice();
	}

	@Override
	public Date getFirstIssuedDate(Long branchId) {
		return feInvoiceRepository.getFirstIssuedDate(branchId);
	}
	
	@Override
	public Date getFirstIssuedDateDs(Long branchId) {
		return feInvoiceRepository.getFirstIssuedDateDs(branchId);
	}

	@Override
	public List<FEInvoice> getIssuedInvoicesByYearMonth(Long year, Long month) {
		return feInvoiceRepository.findIssuedInvoicesByYearMonth(year, month);
	}

}
