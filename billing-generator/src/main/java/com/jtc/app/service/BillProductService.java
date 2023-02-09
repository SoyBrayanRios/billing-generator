package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.BillProduct;

public interface BillProductService {
	public BillProduct saveBillProduct(BillProduct billProduct) throws Exception;
	public List<BillProduct> getBillProducts();
	public void deleteBillProduct(Long billProductId);
	public BillProduct getBillProductById(Long billProductId);
	public List<BillProduct> getByBillId(Long billId);
}
