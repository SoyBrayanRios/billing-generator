package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.Maintenance;

public interface MaintenanceService {
	
	public Maintenance saveMaintenance(Maintenance maintenance) throws Exception;
	public List<Maintenance> getMaintenances();
	public Maintenance getMaintenanceById(Long maintenanceId);
	public Maintenance getMaintenanceByCostFrequency(Long maintenanceCost, Frequency frequency);
	public void deleteMaintenance(Long maintenanceId);

}
