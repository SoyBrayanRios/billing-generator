package com.jtc.app.service;

import java.util.List;

import com.jtc.app.secondary.entity.FETransmitter;

public interface FETransmitterService {

	public List<FETransmitter> getFETransmitters();
	public List<FETransmitter> getFEActiveTransmitters();
	public FETransmitter getTransmitterById(Long transmitterId);
}
