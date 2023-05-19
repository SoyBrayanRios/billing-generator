package com.jtc.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.service.PaymentTypeService;

/**
 * Controlador que expone todas las API que interactuan con la información de cada plan de pago de FE, NE y DS. 
 *
 */
@RestController
@RequestMapping("/api/payment-plan")
public class PaymentTypeController {

	@Autowired
	private PaymentTypeService paymentTypeService;

	/**
	 * API para guardar un plan de pago.
	 * @param paymentType (Plan de pago que se desea guardar)
	 * @return El objeto con los datos del plan de pago guardado en la base de datos.
	 * @throws Exception
	 */
	@PostMapping("/")
	public PaymentType savePlan(@RequestBody PaymentType paymentType) throws Exception {
		return paymentTypeService.savePaymentType(paymentType);
	}

}
