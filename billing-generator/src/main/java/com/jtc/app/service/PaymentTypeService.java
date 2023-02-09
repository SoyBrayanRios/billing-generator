package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.PaymentType;

public interface PaymentTypeService {

	public PaymentType savePaymentType(PaymentType paymentType) throws Exception;
	public PaymentType getPaymentTypeByParams(Integer discriminatorType, String costRange, String packageName, Integer documentQuantity, 
			Long packagePrice, Long docPrice, Frequency paymentFrequencyId, String modulePlan);
	public List<PaymentType> getPaymentTypes();
	public PaymentType findByPackageId(Long packageId);
	
}
