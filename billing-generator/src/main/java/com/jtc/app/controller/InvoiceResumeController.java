package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.InvoiceResume;
import com.jtc.app.service.InvoiceResumeService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/inv-resume")
public class InvoiceResumeController {
	
	@Autowired
	private InvoiceResumeService invoiceResumeService;
	
	@GetMapping("/all")
	public List<InvoiceResume> getAllResumes() {
		return invoiceResumeService.getAllResumes();
	}
	
	@GetMapping("/u")
	public List<InvoiceResume> updateInvoiceResumes() {
		return invoiceResumeService.getAllResumes();
	}
	
	@GetMapping("/{branchId}/{year}/{month}")
	public InvoiceResume getInvoiceResumeRow(@PathVariable("branchId") Long branchId, @PathVariable("year") Long year, @PathVariable("month") Long month) {
		return invoiceResumeService.getInvoiceResumeRow(branchId, year, month);
	}
	
	@GetMapping("/count/{year}/{month}")
	public Integer getInvoiceResumeCount(@PathVariable("year") Long year, @PathVariable("month") Long month) {
		return invoiceResumeService.getInvoiceResumeRowByYearMonth(year, month).size();
	}
	
	@GetMapping("/data-table/{year}")
	public List<String[]> orderResumesToTable(@PathVariable("year") Long year) {
		return invoiceResumeService.orderResumesToTable(year);
	}
	
}
