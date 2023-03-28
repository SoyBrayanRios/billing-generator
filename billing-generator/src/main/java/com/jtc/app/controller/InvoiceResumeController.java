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
	
	@GetMapping("/all/{module}")
	public List<InvoiceResume> getAllResumes(@PathVariable("module") String module) {
		return invoiceResumeService.getAllResumes(module);
	}
	
	@GetMapping("/u/{module}")
	public List<InvoiceResume> updateInvoiceResumes(@PathVariable("module") String module) {
		return invoiceResumeService.getAllResumes(module);
	}
	
	@GetMapping("/{branchId}/{year}/{month}/{module}")
	public InvoiceResume getInvoiceResumeRow(@PathVariable("branchId") Long branchId, @PathVariable("year") Long year, 
			@PathVariable("month") Long month, @PathVariable("module") String module) {
		return invoiceResumeService.getInvoiceResumeRow(branchId, year, month, module);
	}
	
	@GetMapping("/count/{year}/{month}/{module}")
	public Integer getInvoiceResumeCount(@PathVariable("year") Long year, @PathVariable("month") Long month, @PathVariable("module") String module) {
		return invoiceResumeService.getInvoiceResumeRowByYearMonth(year, month, module).size();
	}
	
	@GetMapping("/data-table/{year}/{module}")
	public List<String[]> orderResumesToTable(@PathVariable("year") Long year, @PathVariable("module") String module) {
		return invoiceResumeService.orderResumesToTable(year, module);
	}
	
}
