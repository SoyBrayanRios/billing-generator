package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.secondary.dao.FETransmitterRepository;
import com.jtc.app.secondary.entity.FETransmitter;
import com.jtc.app.service.FETransmitterService;

@Service
public class FETransmitterServiceImpl implements FETransmitterService{

	@Autowired
	private FETransmitterRepository feTransmitterRepository;

	@Override
	public List<FETransmitter> getFETransmitters() {
		return feTransmitterRepository.findAll();
	}

	@Override
	public FETransmitter getTransmitterById(Long transmitterId) {
		return feTransmitterRepository.getById(transmitterId);
	}

	@Override
	public List<FETransmitter> getFEActiveTransmitters() {
		return feTransmitterRepository.getActiveTransmitters();
	}
	
}
