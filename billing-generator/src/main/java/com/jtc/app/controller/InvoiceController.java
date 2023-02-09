package com.jtc.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Invoice;
import com.jtc.app.service.InvoiceService;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@PostMapping("/")
	public Invoice saveInvoice(@RequestBody Invoice invoice) throws Exception {
		return invoiceService.saveInvoice(invoice);
	}

	@GetMapping("/all")
	public List<Invoice> getInvoices() {
		return invoiceService.getInvoices();
	}

	public void deleteInvoice(Long invoiceId) {
		//TODO
	}
	
	public Invoice getInvoiceByBranchNumber(Long branchId, String invoiceNumber, Long buyerId, Date issuedDate) {
		return invoiceService.getInvoiceByBranchNumber(branchId, invoiceNumber, buyerId, issuedDate);
	}

	@GetMapping("/u")
	public List<Invoice> updateAllInvoices() {
		return invoiceService.updateAllInvoices();
	}
	
	@GetMapping("/u/{year}/{month}")
	public List<Invoice> updateInvoicesByYearMonth(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month) {
		return invoiceService.updateInvoicesByYearMonth(year, month);
	}
	
	@GetMapping("/x-contract/{branchId}/{startDate}/{limitDate}")
	public Long getIssuedInvoicesDuringContract(@PathVariable(name = "branchId") Long branchId, 
			@PathVariable(name = "startDate") String startDate, @PathVariable(name = "limitDate") String limitDate) {
		return invoiceService.getIssuedInvoicesDuringContract(branchId, startDate, limitDate);
	}
}
