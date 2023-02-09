package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.BillDetail;
import com.jtc.app.service.BillDetailService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/bill-detail")
public class BillDetailController {

	@Autowired
	private BillDetailService billDetailService;
	
	@GetMapping("/list")
	public List<BillDetail> getBillDetailList() {
		return billDetailService.getBillDetailList();
	}
	
}
