package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

	@Query(value = "select * from maintenance where maintenance_cost=:maintenanceCost and frequency_id=:frequency ;", nativeQuery = true)
	public Maintenance getMaintenanceByCostFrequency(Long maintenanceCost, Frequency frequency);
}
