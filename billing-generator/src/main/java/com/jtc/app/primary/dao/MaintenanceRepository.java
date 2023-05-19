package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.Maintenance;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "maintenance".
 *
 */
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener los datos de un mantenimiento según su costo y frecuencia de pago. 
	 * @param maintenanceCost (Valor del mantenimiento).
	 * @param frequency (Frecuencia de pago).
	 * @return El objeto con los datos del mantenimiento que corresponda a los parámetros dados.
	 */
	@Query(value = "select * from maintenance where maintenance_cost=:maintenanceCost and frequency_id=:frequency ;", nativeQuery = true)
	public Maintenance getMaintenanceByCostFrequency(Long maintenanceCost, Frequency frequency);
}
