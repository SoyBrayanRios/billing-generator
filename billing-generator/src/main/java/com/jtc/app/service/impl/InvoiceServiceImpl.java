package com.jtc.app.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.BranchRepository;
import com.jtc.app.primary.dao.InvoiceRepository;
import com.jtc.app.primary.entity.Invoice;
import com.jtc.app.secondary.dao.FEInvoiceRepository;
import com.jtc.app.secondary.entity.FEInvoice;
import com.jtc.app.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private FEInvoiceRepository feInvoiceRepository;
	@Autowired
	private BranchRepository branchRepository;
	
	@Override
	public Invoice saveInvoice(Invoice invoice) throws Exception {
		return invoiceRepository.save(invoice);
	}

	@Override
	public List<Invoice> getInvoices() {
		return invoiceRepository.findAll();
	}

	@Override
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Long buyerId, Date issuedDate) {
		return invoiceRepository.getInvoiceByBranchNumber(branchId, invoiceNumber, buyerId, issuedDate);
	}

	@Override
	public void deleteInvoice(Long invoiceId) {
		invoiceRepository.deleteById(invoiceId);
	}

	@Override
	public void updateById(Long invoiceId) throws Exception {
		invoiceRepository.updateById(invoiceId);
	}
	
	@Override
	public Long getIssuedInvoicesDuringContract(Long branchId, Date startDate, Date limitDate) {
		return invoiceRepository.getIssuedInvoicesDuringContract(branchId, startDate, limitDate);
	}

	@Override
	public List<Invoice> getInvoicesToCount() {
		return invoiceRepository.getInvoicesToCount();
	}

	@Override
	public List<Invoice> updateAllInvoices() {
		List<FEInvoice> feInvoices = feInvoiceRepository.findAllIssuedInvoice();
		feInvoices.forEach(invoice -> {
			Invoice tempInvoice = invoiceRepository.getInvoiceByBranchNumber(invoice.getTransmitterId(), invoice.getInvoiceNumber(),
					invoice.getClientId(), invoice.getIssueDate());
			if (tempInvoice == null) {
				tempInvoice = new Invoice();
				tempInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
				//tempInvoice.setBuyerId(invoice.getClientId());
				tempInvoice.setIssuedDate(invoice.getIssueDate());
				tempInvoice.setCounted(false);
				try {
					tempInvoice.setBranch(branchRepository.findByBranchId(invoice.getTransmitterId()));
				} catch (Exception e) {
					System.out.println("La sucursal no está registrada en el sistema.");
					e.printStackTrace();
				}
				try {
					invoiceRepository.save(tempInvoice);
					System.out.println("La factura " + tempInvoice.getInvoiceNumber() + " se guardó con éxito.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("La factura " + tempInvoice.getInvoiceNumber() + " ya existe.");
			}
		});
		return invoiceRepository.findAll();
	}

	@Override
	public List<Invoice> updateInvoicesByYearMonth(Long year, Long month) {
		List<FEInvoice> feInvoices = feInvoiceRepository.findIssuedInvoicesByYearMonth(year, month);
		feInvoices.forEach(invoice -> {
			Invoice tempInvoice = invoiceRepository.getInvoiceByBranchNumber(invoice.getTransmitterId(), invoice.getInvoiceNumber(), 
					invoice.getClientId(), invoice.getIssueDate());
			if (tempInvoice == null) {
				tempInvoice = new Invoice();
				tempInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
				//tempInvoice.setBuyerId(invoice.getClientId());
				tempInvoice.setIssuedDate(invoice.getIssueDate());
				tempInvoice.setCounted(false);
				try {
					tempInvoice.setBranch(branchRepository.findByBranchId(invoice.getTransmitterId()));
				} catch (Exception e) {
					System.out.println("La sucursal no está registrada en el sistema.");
					e.printStackTrace();
				}
				try {
					invoiceRepository.save(tempInvoice);
					System.out.println("La factura " + tempInvoice.getInvoiceNumber() + " se guardó con éxito.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("La factura " + tempInvoice.getInvoiceNumber() + " ya existe.");
			}
		});
		return invoiceRepository.findAll();
	}

	@Override
	public Long getIssuedInvoicesDuringContract(Long branchId, String startDate, String limitDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = formatter.parse(limitDate);
			date2 = formatter.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return invoiceRepository.getIssuedInvoicesDuringContract(branchId, date1, date2);
	}

}
