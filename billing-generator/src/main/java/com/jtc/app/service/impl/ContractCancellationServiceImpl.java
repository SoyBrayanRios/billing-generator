package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.CancellationRepository;
import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.ContractCancellation;
import com.jtc.app.service.ContractCancellationService;

@Service
public class ContractCancellationServiceImpl implements ContractCancellationService{

	@Autowired
	private CancellationRepository cancellationRepository;
	
	@Override
	public ContractCancellation saveCancellation(ContractCancellation contractCancellation) throws Exception {
		ContractCancellation cancellation = cancellationRepository.findByContractId(contractCancellation.getContract().getContractId());
		if (cancellation != null) {
			throw new Exception("La cancelación ya se encuentra registrada");
		} else {
			cancellation = cancellationRepository.save(contractCancellation);
		}
		return cancellation;
	}

	@Override
	public List<ContractCancellation> getCancellations() {
		return cancellationRepository.findAll();
	}

	@Override
	public void deleteCancellation(String cancellationId) {
		//TODO
	}

}
