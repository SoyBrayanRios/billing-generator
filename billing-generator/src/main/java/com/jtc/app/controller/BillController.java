package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.JsonResponse;
import com.jtc.app.service.BillService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/billing")
public class BillController {

	@Autowired
	private BillService billService;

	public Bill saveBill(Bill bill) throws Exception {
		return billService.saveBill(bill);
	}

	public List<Bill> getBills() {
		return billService.getBills();
	}

	public void deleteBill(Long billId) {
		billService.deleteBill(billId);
	}

	public Bill getBillById(Long billId) {
		return billService.getBillById(billId);
	}

	@GetMapping("/generate/{year}/{month}/{number}/{env}/{module}")
	public JsonResponse generateBills(@PathVariable("year") Long year, @PathVariable("month") Long month, 
			@PathVariable("number") Long invoiceNumber, @PathVariable("env") String environment, @PathVariable("module") String module) {
		return billService.generateBills(year, month, invoiceNumber, environment, module);
	}
	
	@GetMapping("/fe-faceldi/{year}/{month}/{number}/{env}/{module}")
	public Object[] generateTestBills(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable("number") Long invoiceNumber, @PathVariable("module") String module) {
		return billService.generateTestBills(year, month, invoiceNumber, module);
	}
	
	@GetMapping("/fe-faceldi/{year}/{month}/{env}/{module}")
	public Object[] generateFaceldiFile(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable(name = "env") String environment, @PathVariable("module") String module) {
		return billService.generateFaceldiFile(year, month, environment, module);
	}
	
	@GetMapping("/fe-smart/{year}/{month}/{env}/{module}")
	public Object[] generateSmartFile(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable(name = "env") String environment, @PathVariable("module") String module) {
		return billService.generateSmartFile(year, month, environment, module);
	}
	
	@GetMapping("/fe-smart")
	public Object[] generateTestSmartFile() {
		return billService.getSmartFile();
	}

}
