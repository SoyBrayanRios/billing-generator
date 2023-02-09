package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.MaintenanceRepository;
import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.Maintenance;
import com.jtc.app.service.MaintenanceService;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	MaintenanceRepository maintenanceRepository;
	
	@Override
	public Maintenance saveMaintenance(Maintenance maintenance) throws Exception {
		return maintenanceRepository.save(maintenance);
	}

	@Override
	public List<Maintenance> getMaintenances() {
		return maintenanceRepository.findAll();
	}

	@Override
	public Maintenance getMaintenanceById(Long maintenanceId) {
		return maintenanceRepository.getById(maintenanceId);
	}

	@Override
	public Maintenance getMaintenanceByCostFrequency(Long maintenanceCost, Frequency frequency) {
		return maintenanceRepository.getMaintenanceByCostFrequency(maintenanceCost, frequency);
	}

	@Override
	public void deleteMaintenance(Long maintenanceId) {
		// TODO Auto-generated method stub
	}

}
