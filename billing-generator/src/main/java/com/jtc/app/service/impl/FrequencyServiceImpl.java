package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.FrequencyRepository;
import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.service.FrequencyService;

@Service
public class FrequencyServiceImpl implements FrequencyService {

	@Autowired
	FrequencyRepository frequencyRepository;
	
	@Override
	public Frequency saveFrequency(Frequency frequency) throws Exception {
		return frequencyRepository.save(frequency);
	}

	@Override
	public List<Frequency> getFrequencies() {
		return frequencyRepository.findAll();
	}

	@Override
	public Frequency getFrequencyById(Long frequencyId) {
		return frequencyRepository.getFrequencyById(frequencyId);
	}

}
