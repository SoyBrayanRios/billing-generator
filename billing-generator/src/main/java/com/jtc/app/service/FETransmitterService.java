package com.jtc.app.service;

import java.util.List;

import com.jtc.app.secondary.entity.Transmitter;

public interface FETransmitterService {

	public List<Transmitter> getFETransmitters();
	public List<Transmitter> getFEActiveTransmitters();
	public Transmitter getTransmitterById(Long transmitterId);
}
