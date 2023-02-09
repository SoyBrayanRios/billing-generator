package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.secondary.dao.FEClientRepository;
import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.service.FEClientService;

@Service
public class FEClientServiceImpl implements FEClientService{

	@Autowired
	private FEClientRepository feClientRepository;
	
	@Override
	public List<FEClient> getFEActiveClients() {
		return feClientRepository.getActiveFEClients();
	}

	@Override
	public List<FEClient> getFEClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FEClient getClientById(Long clientId) {
		// TODO Auto-generated method stub
		return null;
	}
}
