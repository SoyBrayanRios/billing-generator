package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.AllianceRepository;
import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.service.AllianceService;

@Service
public class AllianceServiceImpl implements AllianceService{

	@Autowired
	private AllianceRepository allianceRepository;
	
	@Override
	public Alliance saveAlliance(Alliance alliance) throws Exception{
		Alliance tempAlliance = allianceRepository.findByAllianceId(alliance.getAllianceId());
		if (tempAlliance != null) {
			throw new Exception("La alianza ya existe");
		} else {
			allianceRepository.save(alliance);
		}
		return tempAlliance;
	}

	@Override
	public List<Alliance> getAlliances() {
		return allianceRepository.findAll(); 
	}
	
	@Override
	public Alliance getAllianceByName(String allianceName){
		return allianceRepository.findByName(allianceName);
	}

	@Override
	public void deleteAlliance(Long allianceId){
		allianceRepository.deleteById(allianceId);
	}

	@Override
	public Alliance getAllianceById(Long allianceId) {
		return allianceRepository.getById(allianceId);
	}


}
