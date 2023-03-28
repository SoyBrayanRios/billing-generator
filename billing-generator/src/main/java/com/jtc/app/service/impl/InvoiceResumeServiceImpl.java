package com.jtc.app.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.InvoiceRepository;
import com.jtc.app.primary.dao.InvoiceResumeRepository;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.Invoice;
import com.jtc.app.primary.entity.InvoiceResume;
import com.jtc.app.service.InvoiceResumeService;

@Service
public class InvoiceResumeServiceImpl implements InvoiceResumeService {

	@Autowired
	private InvoiceResumeRepository invoiceResumeRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Override
	public InvoiceResume getInvoiceResumeRow(Long branchId, Long year, Long month, String module) {
		return invoiceResumeRepository.findByBranchYearMonth(branchId, year, month, module);
	}

	@Override
	public InvoiceResume saveInvoiceResume(InvoiceResume invoiceResume) throws Exception {
		return invoiceResumeRepository.save(invoiceResume);
	}

	@Override
	public void updateById(Long resumeId, Long issuedInvoices) throws Exception {
		invoiceResumeRepository.updateById(resumeId, issuedInvoices);
	}


	@Override
	public List<InvoiceResume> getInvoiceResumeRowByYearMonth(Long year, Long month, String module) {
		return invoiceResumeRepository.findByYearMonthModule(year, month, module);
	}
	
	@Override
	public List<InvoiceResume> getAllResumes(String module) {
		String[] fE = {"FV", "NC", "FE", "FCD", "FCF", "ND"};
		String[] dS = {"DS", "NAS"};
		String[] nE = {"NI", "NIA"};
		List<Invoice> invoices = invoiceRepository.getInvoicesToCount();
		List<Invoice> filteredInvoices = invoices.stream().filter(i -> Arrays.asList(
				module.equals("FE") ? fE : module.equals("DS") ? dS : nE)
				.contains(i.getDocumentType())).collect(Collectors.toList());

		if (filteredInvoices != null) {
			filteredInvoices.forEach(invoice -> {
				Date tempDate = Optional.ofNullable(invoice.getIssuedDate()).orElse(new Date("1999/12/31"));
				Long year = new Long(tempDate.getYear() + 1900);
				Long month = new Long(tempDate.getMonth() + 1);
				InvoiceResume invoiceResume = getInvoiceResumeRow(invoice.getBranch().getBranchId(), 
						year, month, module);
				if (invoice.getCounted() == false) {
					if (invoiceResume == null) {
						invoiceResume = new InvoiceResume();
						invoiceResume.setYear(year);
						invoiceResume.setMonth(month);
						invoiceResume.setIssuedInvoices(1L);
						invoiceResume.setBranch(invoice.getBranch());
						try {
							saveInvoiceResume(invoiceResume);
							invoiceRepository.updateById(invoice.getInvoiceId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							updateById(invoiceResume.getResumeId(), invoiceResume.getIssuedInvoices() + 1L);
							invoiceRepository.updateById(invoice.getInvoiceId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		return invoiceResumeRepository.findAll();
	}

	@Override
	public List<String[]> orderResumesToTable(Long year, String module) {
		LinkedHashMap<Long, LinkedHashMap<Long, String[]>> response = new LinkedHashMap<>();
		List<String[]> list = new ArrayList<>();
		this.getAllResumes(module).stream().filter(resume -> resume.getYear() == year.longValue()).forEach(resume -> {
			Branch tempBranch = resume.getBranch();
			LinkedHashMap<Long, String[]> tempResume = Optional.ofNullable(response.get(tempBranch.getBranchId())).orElse(null);
			if (tempResume != null) {
				if (tempResume.get(resume.getYear()) != null) {
					response.get(resume.getBranch().getBranchId()).get(resume.getYear())[resume.getMonth().intValue() + 5] = resume.getIssuedInvoices().toString();
				} else {
					String[] pondered = new String[18];
					Arrays.fill(pondered, "0");
					pondered[0] = resume.getBranch().getClient().getNit();
					pondered[1] = resume.getBranch().getClient().getRazonSocial();
					pondered[2] = resume.getBranch().getBranchId().toString();
					pondered[3] = resume.getBranch().getName();
					pondered[4] = resume.getBranch().getClient().getAlliance() != null ? resume.getBranch().getClient().getAlliance().getName() : "DIRECTO";
					pondered[5] = resume.getYear().toString();
					pondered[resume.getMonth().intValue() + 5] = resume.getIssuedInvoices().toString();
					response.get(resume.getBranch().getBranchId()).put(resume.getYear(), pondered);
				}
			} else {
				String[] pondered = new String[18];
				Arrays.fill(pondered, "0");
				pondered[0] = resume.getBranch().getClient().getNit();
				pondered[1] = resume.getBranch().getClient().getRazonSocial();
				pondered[2] = resume.getBranch().getBranchId().toString();
				pondered[3] = resume.getBranch().getName();
				pondered[4] = resume.getBranch().getClient().getAlliance() != null ? resume.getBranch().getClient().getAlliance().getName() : "DIRECTO";
				pondered[5] = resume.getYear().toString();
				pondered[resume.getMonth().intValue() + 5] = resume.getIssuedInvoices().toString();
				LinkedHashMap<Long, String[]> tempMap = new LinkedHashMap<>();
				tempMap.put(resume.getYear(), pondered);
				response.put(resume.getBranch().getBranchId(), tempMap);
			}
		});
		for (Long key: response.keySet()) {
			Set<Long> keys = response.get(key).keySet();
			for (Long yearKey: keys) {
				list.add(response.get(key).get(yearKey));				
			}
		}
		return list;
	}

}
