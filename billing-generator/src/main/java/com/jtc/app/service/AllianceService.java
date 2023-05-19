package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Alliance;

/**
 * Esta interface define los servicios relacionados a la clase Alliance.
 *
 */
public interface AllianceService {
	
	public Alliance saveAlliance(Alliance alliance) throws Exception;
	public List<Alliance> getAlliances();
	public Alliance getAllianceByName(String allianceName);
	public void deleteAlliance(Long allianceId);
	public Alliance getAllianceById(Long allianceId);
	
}
