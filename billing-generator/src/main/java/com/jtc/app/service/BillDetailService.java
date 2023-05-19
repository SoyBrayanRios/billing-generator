package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.BillDetail;

/**
 * Esta clase define los servicios relacionados a la clase BillDetail.
 *
 */
public interface BillDetailService {
	
	public List<BillDetail> getBillDetailList();
	public BillDetail saveBillDetail(BillDetail billDetail) throws Exception;
}
