package com.jtc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.service.PaymentTypeService;

@RestController
@RequestMapping("/api/payment-plan")
public class PaymentTypeController {

	@Autowired
	PaymentTypeService paymentTypeService;

	@PostMapping("/")
	public PaymentType savePlan(@RequestBody PaymentType paymentType) throws Exception {
		return paymentTypeService.savePaymentType(paymentType);
	}

}
