package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.PaymentType;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "payment_type".
 *
 */
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener un plan de pago que coincida con los parámetros dados.
	 * @param discriminatorType (1 - Por paquete de documentos FE || Bolsa de documentos FE, 2 - Por documentos emitidos FE, 3 - Por rango mensual FE,
	 * 4 - Por documentos emitidos NE, 5 - Por paquete de empleados NE, 6 - Por paquete de documentos DS).
	 * @param costRange (JSON con los rangos de cobro).
	 * @param packageName (Nombre del paquete)
	 * @param documentQuantity (Cantidad de documentos del paquete).
	 * @param packagePrice (Valor del paquete).
	 * @param docPrice (Valor por documento adicional emitido).
	 * @param paymentFrequencyId (Frecuencia de cobro).
	 * @param modulePlan (Acrónimo que indica a cual servicio corresponde el plan, bien sea FE, NE o DS).
	 * @param mixedContract (true si se cobra mensualidad + documento emitido o false si no se debe cobrar en esa modalidad).
	 * @param selfAdjusting (true si el paquete se ajusta mensualmente según la cantidad de documentos emitidos, de lo contrario false).
	 * @return El objeto con los datos del plan de pago.
	 */
	@Query(value = "select * from payment_plan where discriminator_type=:discriminatorType and document_price=:docPrice and"
			+ "(cost_range=:costRange or cost_range isnull) and (package_name=:packageName or package_name isnull) and document_quantity=:documentQuantity and "
			+ "package_price=:packagePrice and payment_frequency_id=:paymentFrequencyId and module_plan=:modulePlan "
			+ "and mixed_contract=:mixedContract and self_adjusting=:selfAdjusting", nativeQuery = true)
	public PaymentType findPackageByParams(Integer discriminatorType, String costRange, String packageName, Integer documentQuantity, 
			Long packagePrice, Long docPrice, Frequency paymentFrequencyId, String modulePlan, Boolean mixedContract, Boolean selfAdjusting);
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener un plan de pago según su ID.
	 * @param packageId (ID del paquete).
	 * @return El objeto con los datos del plan de pago que coincida con el ID suministrado.
	 */
	@Query(value = "select * from payment_plan where payment_plan_id=:packageId", nativeQuery = true)
	public PaymentType findByPackageId(Long packageId);
	
}
