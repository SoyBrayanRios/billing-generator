package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.BillProductRepository;
import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.service.BillProductService;

@Service
public class BillProductServiceImpl implements BillProductService {

	@Autowired
	private BillProductRepository billProductRepository;
	
	public BillProduct saveBillProduct(BillProduct billProduct) throws Exception {
		return billProductRepository.save(billProduct);
	}

	public List<BillProduct> getBillProducts() {
		return billProductRepository.findAll();
	}

	public void deleteBillProduct(Long billProductId) {
		billProductRepository.deleteById(billProductId);
	}

	public BillProduct getBillProductById(Long billProductId) {
		return billProductRepository.getById(billProductId);
	}

	@Override
	public List<BillProduct> getByBillId(Long billId) {
		return billProductRepository.getByBillId(billId);
	}

}
