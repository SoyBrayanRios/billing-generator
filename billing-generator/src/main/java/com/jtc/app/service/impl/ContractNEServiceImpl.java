package com.jtc.app.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.BranchRepository;
import com.jtc.app.primary.dao.ContractNERepository;
import com.jtc.app.primary.dao.MaintenanceRepository;
import com.jtc.app.primary.dao.PaymentTypeRepository;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.ContractNE;
import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.service.ContractNEService;
import com.jtc.app.service.FrequencyService;
import com.jtc.app.service.NEInvoiceService;

@Service
public class ContractNEServiceImpl implements ContractNEService {

	@Autowired
	private ContractNERepository contractRepository;
	@Autowired
	private PaymentTypeRepository paymentTypeRepository;
	@Autowired
	private BranchRepository branchRepository;
	@Autowired
	private NEInvoiceService neInvoiceService;
	@Autowired
	private FrequencyService frequencyService;

	@Override
	public ContractNE saveContract(ContractNE contract) throws Exception {
		// Validate if the contract exists
		ContractNE exists = contractRepository.getContractById(contract.getContractId());
		if (exists != null) {
			System.out.println("El contrato que intenta guardar ya existe");
			return exists;
		}
		// Validate if the paymentPlan exists
		PaymentType tempPlan = contract.getPaymentPlan();
		PaymentType paymentPlan = paymentTypeRepository.findPackageByParams(tempPlan.getDiscriminatorType(),
				tempPlan.getCostRange(), tempPlan.getPackageName(), tempPlan.getDocumentQuantity(),
				tempPlan.getPackagePrice(), tempPlan.getDocumentPrice(), tempPlan.getPaymentFrequency(),
				tempPlan.getModulePlan(), tempPlan.getMixedContract(), tempPlan.getSelfAdjusting());
		if (paymentPlan == null) {
			if (tempPlan.getCostRange() == "") {
				tempPlan.setCostRange(null);
			}
			if (tempPlan.getPackageName() == "") {
				tempPlan.setPackageName(null);
			}
			tempPlan.generateFePlanDescription();
			paymentPlan = paymentTypeRepository.save(tempPlan);
		}
		// Save changes different from contract
		contract.setPaymentPlan(paymentPlan);
		// contract.setMaintenanceType(maintenancePlan);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(contract.getContractDate());
		calendar.add(Calendar.DATE, 1);
		contract.setContractDate(calendar.getTime());
		return contractRepository.save(contract);
	}

	@Override
	public List<ContractNE> getContracts() {
		return contractRepository.findAll();
	}

	@Override
	public ContractNE getContractById(String contractId) {
		return contractRepository.getContractById(contractId);
	}

	@Override
	public ContractNE getContractByBranch(Long branchId) {
		return contractRepository.findByBranchId(branchId);
	}

	@Override
	public void deleteContract(String contractId) {
		contractRepository.deleteById(contractId);
	}

	@Override
	public List<String> getSharedContracts() {
		return contractRepository.getSharedContracts();
	}

	public List<String[]> getArrayFromFile(String filePath) {
		List<String[]> tempArray = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));

			String line = br.readLine();
			while (line != null) {
				String fields[] = line.split(";");
				for (int i = 0; i < fields.length; i++) {
					fields[i] = fields[i].replaceAll("~", "");
					fields[i] = fields[i].replaceAll("%", "");
					if ((i == 11 || i == 21 || i == 25) && fields[i] != null && fields[i] != "") {
						fields[i] = fields[i].replace("$", "");
						fields[i] = fields[i].replace(".", "");
					}
					fields[i] = fields[i].trim();
				}
				tempArray.add(fields);
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tempArray;
	}

	public List<ContractNE> saveContractsFromFile(String filePath) {
		List<ContractNE> contracts = new ArrayList<>();
		List<String[]> tempArray = this.getArrayFromFile(filePath);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		tempArray.forEach(array -> {
			// Get branch by nit
			ContractNE contract = contractRepository.getContractById(array[6]);

			if (contract == null) {
				contract = new ContractNE();
				Branch branch = branchRepository.findByBranchId(Long.parseLong(array[3]));
				PaymentType paymentType = null;
				
				contract.setBranch(branch);
				contract.setContractId(array[6]);
				try {
					contract.setContractDate(formatter.parse(array[7]));
					contract.setReferencePaymentDate(array[36] != null && array[36] != "" ? formatter.parse(array[36]): null);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				contract.setFirstIssueDate(neInvoiceService.getFirstIssuedDate(branch.getBranchId()));
				contract.setCreatedBy(array[8]);
				contract.setIpcIncrease(array[9].equalsIgnoreCase("Si") ? true : false);
				contract.setImplementationCost(Long.parseLong(array[11]));
				contract.setImplementationAlreadyPaid(array[32].equalsIgnoreCase("No") ? true : false);
				contract.setSharedContract(array[16].equalsIgnoreCase("Si") ? true : false);
				contract.setPrepaid(false);
				contract.setFirstYearFree(array[12].equalsIgnoreCase("Si") ? true : false);
				contract.setFirstMonthFree(array[13].equalsIgnoreCase("Si") ? true : false);
				contract.setDiscountSecondYear(array[14] != null && array[14] != "" ? Long.parseLong(array[14]) : 0L);
				try {
					contract.setQualificationDate(array[29] != "" && array[29] != null ? formatter.parse(array[29]) : null);
				} catch(Exception e) {
					contract.setQualificationDate(null);
					e.printStackTrace();
				}
				// Validate if it is a shared contract
				if (array[13].equalsIgnoreCase("Si")) {
					contract.setSharedContractId(array[17]);
				} else {
					contract.setSharedContractId(null);
					// Get payment
					if (array[12].equalsIgnoreCase("Por paquete de empleados")) {
						Long id = 0L;
						if (array[22].equalsIgnoreCase("Mensual")) {
							id = 4L;
						} else {
							id = 8L;
						}

						Frequency frequency = frequencyService.getFrequencyById(id);

						paymentType = paymentTypeRepository.findPackageByParams(4, null, array[18],
								Integer.parseInt(array[20]), Long.parseLong(array[21]),
								(array[25] == "" || array[25] == null) ? 520L : Long.parseLong(array[25]), frequency,
								"NE", false, false);

						if (paymentType == null) {
							paymentType = new PaymentType();
							paymentType.setDiscriminatorType(4);
							paymentType.setModulePlan("NE");
							paymentType.setPackageName(array[18] != null ? array[18] : null);
							paymentType.setDocumentQuantity(Integer.parseInt(array[20]));
							paymentType.setPackagePrice(Long.parseLong(array[21]));
							paymentType.setDocumentPrice(
									(array[25] == "" || array[25] == null) ? 520L : Long.parseLong(array[25]));
							paymentType.setPaymentFrequency(frequency);
							paymentType.generateNePlanDescription(array[19].toLowerCase());
							paymentType.setCostRange(null);
							paymentType.setMixedContract(false);
							paymentType.setSelfAdjusting(false);
							try {
								contract.setPaymentPlan(paymentType);
								//contract.setPaymentPlan(paymentTypeRepository.save(paymentType));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							contract.setPaymentPlan(paymentType);
						}
					} else {
						System.out.println("Tiene otro tipo de payment_plan");
					}
				}

				try {
					//contractRepository.save(contract);
					System.out.println("Se guardó el contrato " + array[6] + " correctamente.");
					System.out.println(contract.toString());
				} catch (Exception e) {
					System.out.println("Hubo un error al intentar guardar el contrato " + array[6] + " - " + array[1]);
					e.printStackTrace();
				}
			} else {
				System.out.println("El contrato " + array[6] + " ya está registrado en el sistema.");
			}
			contracts.add(contract);
		});
		return contracts;
	}
}