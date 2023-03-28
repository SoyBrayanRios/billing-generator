package com.jtc.app.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;

import com.jtc.app.model.BaseContract;
import com.jtc.app.primary.dao.ContractDSRepository;
import com.jtc.app.primary.dao.ContractFERepository;
import com.jtc.app.primary.dao.ContractNERepository;
import com.jtc.app.primary.entity.ContractDS;
import com.jtc.app.primary.entity.ContractFE;
import com.jtc.app.primary.entity.ContractNE;
import com.jtc.app.service.ContractDSService;
import com.jtc.app.service.ContractFEService;
import com.jtc.app.service.ContractNEService;
import com.jtc.app.service.InvoiceService;

import lombok.Data;

@Controller
@Configurable
@Data
public class ContractFactory {
	
	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private ContractFEService contractFEService;
	@Autowired
	private ContractDSService contractDSService;
	@Autowired
	private ContractNEService contractNEService;
	@Autowired
	private ContractFERepository contractFERepository;
	@Autowired
	private ContractDSRepository contractDSRepository;
	@Autowired
	private ContractNERepository contractNERepository;
	
	public BaseContract getContractType(String module) {
		switch (module) {
			case "FE": return new ContractFE();
			case "DS": return new ContractDS();
			case "NE": return new ContractNE();
			default: return null;
		}
	}
	
	public BaseContract getContractByBranch(String module, Long branchId) {
		switch (module) {
			case "FE": return contractFEService.getContractByBranch(branchId);
			case "DS": return contractDSService.getContractByBranch(branchId);
			case "NE": return contractNEService.getContractByBranch(branchId);
			default: return null;
		}
	}
	
	public List<? extends BaseContract> getModuleContracts(String module) {
		switch (module) {
			case "FE": return contractFEService.getContracts();
			case "DS": return contractDSService.getContracts();
			case "NE": return contractNEService.getContracts();
			default: return new ArrayList<BaseContract>();
		}
	}
	
	public List<String> getSharedContracts(String module) {
		switch (module) {
			case "FE": return contractFEService.getSharedContracts();
			case "DS": return contractDSService.getSharedContracts();
			case "NE": return contractNEService.getSharedContracts();
			default: return new ArrayList<>();
		}
	}
	
	public Long getIssuedInvoicesDuringContract(String module, Long branch, Date startDate, Date limitDate) {
		switch (module) {
			case "FE": return invoiceService.getFeIssuedInvoicesDuringContract(branch, startDate, limitDate);
			case "DS": return invoiceService.getDsIssuedInvoicesDuringContract(branch, startDate, limitDate);
			case "NE": return invoiceService.getNeIssuedInvoicesDuringContract(branch, startDate, limitDate);
			default: return 0L;
		}
	}
	
	public void saveContract(BaseContract contract) {
		try {
			if (contract instanceof ContractFE) {
				contractFERepository.save((ContractFE)contract);
			} else if (contract instanceof ContractDS) {
				contractDSRepository.save((ContractDS)contract);
			} else if (contract instanceof ContractNE) {
				contractNERepository.save((ContractNE)contract);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
