package com.jtc.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.service.BillProductService;

@RestController
public class BillProductController {

	private BillProductService billProductService;
	
	public BillProduct saveBillProduct(BillProduct billProduct) throws Exception {
		return billProductService.saveBillProduct(billProduct);
	}

	public List<BillProduct> getBillProducts() {
		return billProductService.getBillProducts();
	}

	public void deleteBillProduct(Long billProductId) {
		billProductService.deleteBillProduct(billProductId);
	}

	public BillProduct getBillProductById(Long billProductId) {
		return billProductService.getBillProductById(billProductId);
	}
	
}
