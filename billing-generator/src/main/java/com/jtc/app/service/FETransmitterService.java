package com.jtc.app.service;

import java.util.List;

import com.jtc.app.secondary.entity.Transmitter;

/**
 * Esta interface define los servicios relacionados a la clase FETransmitter.
 *
 */
public interface FETransmitterService {

	public List<Transmitter> getFETransmitters();
	public List<Transmitter> getFEActiveTransmitters();
	public Transmitter getTransmitterById(Long transmitterId);
}
