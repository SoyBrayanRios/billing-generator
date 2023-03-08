package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.PaymentType;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

	@Query(value = "select * from payment_plan where discriminator_type=:discriminatorType and document_price=:docPrice and"
			+ "(cost_range=:costRange or cost_range isnull) and (package_name=:packageName or package_name isnull) and document_quantity=:documentQuantity and "
			+ "package_price=:packagePrice and payment_frequency_id=:paymentFrequencyId and module_plan=:modulePlan "
			+ "and mixed_contract=:mixedContract and self_adjusting=:selfAdjusting", nativeQuery = true)
	public PaymentType findPackageByParams(Integer discriminatorType, String costRange, String packageName, Integer documentQuantity, 
			Long packagePrice, Long docPrice, Frequency paymentFrequencyId, String modulePlan, Boolean mixedContract, Boolean selfAdjusting);
	
	@Query(value = "select * from payment_plan where payment_plan_id=:packageId", nativeQuery = true)
	public PaymentType findByPackageId(Long packageId);
	
}
