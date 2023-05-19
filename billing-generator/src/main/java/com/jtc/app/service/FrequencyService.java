package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Frequency;

/**
 * Esta interface define los servicios relacionados a la clase Frequency.
 *
 */
public interface FrequencyService {
	public Frequency saveFrequency(Frequency frequency) throws Exception;
	public List<Frequency> getFrequencies();
	public Frequency getFrequencyById(Long frequencyId);
}
