package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.PaymentTypeRepository;
import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.service.PaymentTypeService;


@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

	@Autowired
	PaymentTypeRepository paymentTypeRepository;

	@Override
	public PaymentType savePaymentType(PaymentType paymentType) throws Exception {
		Integer discriminatorType = paymentType.getDiscriminatorType();
		PaymentType exists = this.getPaymentTypeByParams(discriminatorType, paymentType.getCostRange(), paymentType.getPackageName(),
				paymentType.getDocumentQuantity(), paymentType.getPackagePrice(), paymentType.getDocumentPrice(),
				paymentType.getPaymentFrequency(), paymentType.getModulePlan(), paymentType.getMixedContract(), paymentType.getSelfAdjusting());
		if (exists != null) {
			System.out.println("El plan que intenta guardar ya existe");
			return exists;
		} else {
			if (paymentType.getCostRange() == "") {
				paymentType.setCostRange(null);
			}
			if (paymentType.getPackageName() == "") {
				paymentType.setPackageName(null);
			}
			return paymentTypeRepository.save(paymentType);
		}
	}

	@Override
	public List<PaymentType> getPaymentTypes() {
		return paymentTypeRepository.findAll();
	}

	@Override
	public PaymentType getPaymentTypeByParams(Integer discriminatorType, String costRange, String packageName, Integer documentQuantity, 
			Long packagePrice, Long docPrice, Frequency paymentFrequencyId, String modulePlan, Boolean mixedContract, Boolean selfAdjusting) {
		return paymentTypeRepository.findPackageByParams(discriminatorType, costRange, packageName, documentQuantity, packagePrice,
				docPrice ,paymentFrequencyId, modulePlan, mixedContract, selfAdjusting);
	}

	@Override
	public PaymentType findByPackageId(Long packageId) {
		return paymentTypeRepository.findByPackageId(packageId);
	}

}
