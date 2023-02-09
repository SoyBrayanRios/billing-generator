package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.BillDetailRepository;
import com.jtc.app.primary.entity.BillDetail;
import com.jtc.app.service.BillDetailService;

@Service
public class BillDetailServiceImpl implements BillDetailService {

	@Autowired
	private BillDetailRepository billDetailRepository;
	
	@Override
	public List<BillDetail> getBillDetailList() {
		return billDetailRepository.findAll();
	}

	@Override
	public BillDetail saveBillDetail(BillDetail billDetail) throws Exception {
		return billDetailRepository.save(billDetail);
	}

}
