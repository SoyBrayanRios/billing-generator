package com.jtc.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.secondary.entity.FEInvoice;
import com.jtc.app.service.FEInvoiceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/invoice")
public class FEInvoiceController {

	@Autowired
	private FEInvoiceService feInvoiceService;
	
	@GetMapping("/issued")
	public List<FEInvoice> getIssuedInvoices() {
		return feInvoiceService.getIssuedInvoices();
	}
	
	@GetMapping("/first/{branchId}")
	public Date getFirstIssuedDate(@PathVariable(name = "branchId") Long branchId) {
		return feInvoiceService.getFirstIssuedDate(branchId);
	}
	
}
