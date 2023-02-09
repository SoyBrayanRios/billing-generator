package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.BranchRepository;
import com.jtc.app.primary.dao.ClientRepository;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.secondary.dao.FETransmitterRepository;
import com.jtc.app.secondary.entity.FETransmitter;
import com.jtc.app.service.BranchService;

@Service
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchRepository branchRepository;
	@Autowired
	private FETransmitterRepository feTransmitterRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public Branch saveBranch(Branch branch) throws Exception {
		return branchRepository.save(branch);
	}

	@Override
	public List<Branch> getBranches() {
		return branchRepository.findAll();
	}

	@Override
	public Branch getBranchById(Long branchId) {
		Branch branch = branchRepository.findByBranchId(branchId);
		if (branch == null) {
			throw new RuntimeException("El id de la sucursal " + branchId + " no fue encontrado.");
		}
		return branch;
	}

	@Override
	public void deleteBranch(Long branchId) {
		Branch branch = branchRepository.findByBranchId(branchId);
		if (branch == null) {
			throw new RuntimeException("El id de la sucursal " + branchId + " no fue encontrado.");
		}
		branchRepository.deleteById(branchId);
	}
	
	@Override
	public List<Branch> getBranchesWithoutContractByMonthYear() {
		return branchRepository.findBranchesWithoutContract();
	}
	
	@Override
	public Branch findByClientId(String clientId) {
		return branchRepository.findByClientId(clientId);
	}

	@Override
	public List<Branch> updateAllBranches() {
		List<FETransmitter> feTransmitters = feTransmitterRepository.getActiveTransmitters();
		feTransmitters.forEach(transmitter -> {
			Branch tempBranch = branchRepository.findByBranchId(transmitter.getIdSucursal());
			if (tempBranch == null) {
				tempBranch = new Branch();
				tempBranch.setBranchId(transmitter.getIdSucursal());
				tempBranch.setActive(true);
				tempBranch.setCode(transmitter.getCodigo());
				tempBranch.setName(transmitter.getNombre());
				tempBranch.setCountry(transmitter.getPais());
				tempBranch.setDepartment(transmitter.getDepartamento());
				tempBranch.setDepartmentName(transmitter.getNombreDepartamento());
				tempBranch.setState(transmitter.getMunicipio());
				tempBranch.setStateName(transmitter.getNombreMunicipio());
				tempBranch.setCenter(transmitter.getCentro());
				tempBranch.setAddress(transmitter.getDireccion());
				tempBranch.setAddressComplement(transmitter.getComplementoDireccion());
				tempBranch.setPhone(transmitter.getTelefono());
				tempBranch.setEmail(transmitter.getCorreoElectronico());
				tempBranch.setClient(clientRepository.findByNit(transmitter.getNroIdentificacion()));
				try {
					branchRepository.save(tempBranch);
					System.out.println("Se guardó con exito la sucursal " + tempBranch.getBranchId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("La sucursal " + tempBranch.getBranchId() + " ya existe ");
			}
		});
		return branchRepository.findAll();
	}

}
