package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.primary.entity.JsonResponse;

public interface BillService {

	public Bill saveBill(Bill bill) throws Exception;
	public List<Bill> getBills();
	public void deleteBill(Long billId);
	public Bill getBillById(Long billId);
	public List<Bill> getFaceldiReport(Long year, Long month);
	public JsonResponse generateBills(Long year, Long month, Long invoiceNumber, String environment);
	public Object[] generateTestBills(Long year, Long month, Long invoiceNumber);
	public Object[] generateFaceldiFile(Long year, Long month, String simulation);
	public Object[] generateSmartFile(Long year, Long month, String simulation);
	public Object[] getSmartFile();
}
