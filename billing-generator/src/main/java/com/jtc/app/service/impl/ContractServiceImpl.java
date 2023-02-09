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
import com.jtc.app.primary.dao.ContractRepository;
import com.jtc.app.primary.dao.MaintenanceRepository;
import com.jtc.app.primary.dao.PaymentTypeRepository;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.Contract;
import com.jtc.app.primary.entity.Custody;
import com.jtc.app.primary.entity.Frequency;
import com.jtc.app.primary.entity.Maintenance;
import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.service.ContractService;
import com.jtc.app.service.FEInvoiceService;
import com.jtc.app.service.FrequencyService;

@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private PaymentTypeRepository paymentTypeRepository;
	@Autowired
	private MaintenanceRepository maintenanceRepository;
	@Autowired
	private BranchRepository branchRepository;
	@Autowired
	private FEInvoiceService feInvoiceService;
	@Autowired
	private FrequencyService frequencyService;
	
	@Override
	public Contract saveContract(Contract contract) throws Exception {
		//Validate if the contract exists
				Contract exists = contractRepository.getContractById(contract.getContractId());
				if (exists != null) {
					System.out.println("El contrato que intenta guardar ya existe");
					return exists;
				}
				//Validate if the paymentPlan exists
				PaymentType tempPlan = contract.getPaymentPlan();
				PaymentType paymentPlan = paymentTypeRepository.findPackageByParams(
						tempPlan.getDiscriminatorType(), tempPlan.getCostRange(), tempPlan.getPackageName(), tempPlan.getDocumentQuantity(), 
						tempPlan.getPackagePrice(), tempPlan.getDocumentPrice(), tempPlan.getPaymentFrequency(), tempPlan.getModulePlan());
				if (paymentPlan == null) {
					if (tempPlan.getCostRange() == "") {
						tempPlan.setCostRange(null);
					}
					if (tempPlan.getPackageName() == "") {
						tempPlan.setPackageName(null);
					}
					tempPlan.generatePlanDescription();
					paymentPlan = paymentTypeRepository.save(tempPlan);
				}
				//Validate if the maintenanceType exists
				Maintenance tempMaintenance = contract.getMaintenanceType();
				Maintenance maintenancePlan = maintenanceRepository.getMaintenanceByCostFrequency(tempMaintenance.getMaintenanceCost(), tempMaintenance.getMaintenanceFrequency());
				if (maintenancePlan == null) {
					maintenancePlan = maintenanceRepository.save(tempMaintenance);
				}
				//Save changes different from contract
				contract.setPaymentPlan(paymentPlan);
				contract.setMaintenanceType(maintenancePlan);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(contract.getContractDate());
				calendar.add(Calendar.DATE, 1);
				contract.setContractDate(calendar.getTime());
		return contractRepository.save(contract);
	}

	@Override
	public List<Contract> getContracts() {
		return contractRepository.findAll();
	}

	@Override
	public Contract getContractById(String contractId) {
		return contractRepository.getContractById(contractId);
	}
	
	@Override
	public Contract getContractByBranch(Long branchId) {
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
					/*if (i == 21 && fields[i] != null && fields[i] != "") {
						fields[i] = fields[i].substring(1, fields[i].length() - 1);
					}*/
					if ((i == 11 || i == 17 || i == 19 || i == 20 || i == 22) && fields[i] != null && fields[i] != "") {
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
	
	public List<Contract> saveContractsFromFile(String filePath) {
		List<Contract> contracts = new ArrayList<>();
		List<String[]> tempArray = this.getArrayFromFile(filePath);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		tempArray.forEach(array -> {
			//Get branch by nit
			Contract contract = contractRepository.getContractById(array[6]);

			if (contract == null) {
				contract = new Contract();
				Branch branch = branchRepository.findByBranchId(Long.parseLong(array[3]));
				PaymentType paymentType = null;
				Custody custody = null;
				
				contract.setBranch(branch);
				contract.setCustodyType(custody);
				contract.setContractId(array[6]);
				try {
					contract.setContractDate(formatter.parse(array[7]));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				contract.setFirstIssueDate(feInvoiceService.getFirstIssuedDate(branch.getBranchId()));
				contract.setCreatedBy(array[8]);
				contract.setIpcIncrease(array[9].equalsIgnoreCase("Si") ? true: false);
				contract.setImplementationCost(Long.parseLong(array[11]));
				contract.setImplementationAlreadyPaid(array[31].equalsIgnoreCase("No") ? true: false);
				contract.setSharedContract(array[13].equalsIgnoreCase("Si") ? true: false);
				contract.setPrepaid(false);
				//Validate if it is a shared contract
				if (array[13].equalsIgnoreCase("Si")) {
					contract.setSharedContractId(array[14]);	
				} else {
					contract.setSharedContractId(null);
					//Get payment
					if (array[12].equalsIgnoreCase("Bolsa de documentos") || array[12].equalsIgnoreCase("Por paquete de documentos")) {
						if (array[15] != "" || array[15] != null) {
							paymentType = getPaymentTypeByName(array[15]);
						}
						Long id = 0L;
						if (array[18].equalsIgnoreCase("Mensual")) {
							id = 4L;
						} else {
							id = 8L;
						}
						Frequency frequency = frequencyService.getFrequencyById(id);
						if (paymentType == null) {
							paymentType = paymentTypeRepository.findPackageByParams(1, array[34].equalsIgnoreCase("SI") ? array[21] : "", 
									array[15], Integer.parseInt(array[16]), 
									Long.parseLong(array[17]), (array[19] == "" || array[19] == null) ? 620L: Long.parseLong(array[19])
											, frequency, "FE");
						}
						if (paymentType == null) {
							paymentType = new PaymentType();
							paymentType.setSelfAdjusting(array[34].equalsIgnoreCase("SI") ? true : false);
							paymentType.setDiscriminatorType(1);
							paymentType.setModulePlan("FE");
							paymentType.setPackageName(paymentType.getSelfAdjusting() ? array[15] : null);
							paymentType.setDocumentQuantity(Integer.parseInt(array[16]));
							paymentType.setPackagePrice(Long.parseLong(array[17]));
							paymentType.setDocumentPrice((array[19] == "" || array[19] == null) ? 620L: Long.parseLong(array[19]));
							paymentType.setPaymentFrequency(frequency);
							paymentType.generatePlanDescription();
							paymentType.setCostRange(paymentType.getSelfAdjusting() ? array[21] : null);
							paymentType.setMixedContract(array[35].equalsIgnoreCase("Si") ? true: false);
							try {
								//contract.setPaymentPlan(paymentType);
								contract.setPaymentPlan(paymentTypeRepository.save(paymentType));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							contract.setPaymentPlan(paymentType);
						}
					} else if (array[12].equalsIgnoreCase("Por documentos emitidos")) {
						Frequency frequency = frequencyService.getFrequencyById(4L);
						paymentType = paymentTypeRepository.findPackageByParams(2, "", "", 0, 
								array[35].equalsIgnoreCase("Si") ? Long.parseLong(array[17]): 0L, 
										(array[20] == "" || array[20] == null) ? 620L: Long.parseLong(array[20]), frequency, "FE");
						if (paymentType == null) {
							paymentType = new PaymentType();
							paymentType.setMixedContract(array[35].equalsIgnoreCase("Si") ? true: false);
							paymentType.setDiscriminatorType(2);
							paymentType.setModulePlan("FE");
							paymentType.setPackageName(null);
							paymentType.setDocumentQuantity(0);
							paymentType.setPackagePrice(paymentType.getMixedContract() ? Long.parseLong(array[17]) : 0L);
							paymentType.setDocumentPrice((array[20] == "" || array[20] == null) ? 620L: Long.parseLong(array[20]));
							paymentType.setPaymentFrequency(frequency);
							paymentType.setCostRange(null);
							paymentType.generatePlanDescription();
							paymentType.setSelfAdjusting(array[34].equalsIgnoreCase("SI") ? true : false);
							try {
								//contract.setPaymentPlan(paymentType);
								contract.setPaymentPlan(paymentTypeRepository.save(paymentType));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							contract.setPaymentPlan(paymentType);
						}
					} else if (array[12].equalsIgnoreCase("Por rango mensual")) {
						Frequency frequency = frequencyService.getFrequencyById(4L);
						paymentType = paymentTypeRepository.findPackageByParams(3, array[21], "", 0, 
								0L, 0L, frequency, "FE");
						if (paymentType == null) {
							paymentType = new PaymentType();
							paymentType.setDiscriminatorType(3);
							paymentType.setModulePlan("FE");
							paymentType.setPackageName(null);
							paymentType.setDocumentQuantity(0);
							paymentType.setPackagePrice(0L);
							paymentType.setDocumentPrice(0L);
							paymentType.setPaymentFrequency(frequency);
							paymentType.setCostRange(array[21]);
							paymentType.generatePlanDescription();
							paymentType.setSelfAdjusting(array[34].equalsIgnoreCase("Si") ? true : false);
							paymentType.setMixedContract(array[35].equalsIgnoreCase("Si") ? true: false);
							try {
								//contract.setPaymentPlan(paymentType);
								contract.setPaymentPlan(paymentTypeRepository.save(paymentType));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							contract.setPaymentPlan(paymentType);
						}
					}
				}
				
				// Set maintenance
				// Validate if it is a shared maintenance
				if (array[26].equalsIgnoreCase("No")) {
					Long id = 0L;
					if (array[23].equalsIgnoreCase("Mensual")) {
						id = 4L;
					} else {
						id = 8L;
					}
					Frequency frequency = frequencyService.getFrequencyById(id);
					Maintenance maintenance = maintenanceRepository.getMaintenanceByCostFrequency(Long.parseLong(array[22]), frequency);
					if (maintenance == null) {
						try {
							maintenance = new Maintenance();
							maintenance.setMaintenanceCost(Long.parseLong(array[22]));
							maintenance.setMaintenanceFrequency(frequency);
							//contract.setMaintenanceType(maintenance);
							contract.setMaintenanceType(maintenanceRepository.save(maintenance));
							contract.setMaintenanceAlreadyPaid(array[25].equalsIgnoreCase("Si") ? false : true);
							contract.setSharedMaintenance(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						contract.setMaintenanceType(maintenance);
						contract.setMaintenanceAlreadyPaid(array[25].equalsIgnoreCase("Si") ? false : true);
						contract.setSharedMaintenance(false);
					}
				} else {
					contract.setMaintenanceType(null);
					contract.setMaintenanceAlreadyPaid(false);
					contract.setSharedMaintenance(true);
				}
				try {
					contractRepository.save(contract);
					System.out.println("Se guardó el contrato " + array[6] + " correctamente.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Hubo un error al intentar guardar el contrato " + array[6] + " - " + array[1]);
				}
			} else {
				System.out.println("El contrato " + array[6] + " ya está registrado en el sistema.");
			}
			contracts.add(contract);
		});
		return contracts;
	}
	
	public PaymentType getPaymentTypeByName(String name) {
		switch (name) {
			case "Fantasía": return paymentTypeRepository.findByPackageId(1L); 
			case "Bronce": return paymentTypeRepository.findByPackageId(2L);
			case "Plata": return paymentTypeRepository.findByPackageId(3L);
			case "Oro": return paymentTypeRepository.findByPackageId(4L);
			case "Bolsa 1": return paymentTypeRepository.findByPackageId(5L);
			case "Bolsa 2": return paymentTypeRepository.findByPackageId(6L);
			case "Bolsa 3": return paymentTypeRepository.findByPackageId(7L);
			case "Independientes": return paymentTypeRepository.findByPackageId(8L);
			case "Emprendedores": return paymentTypeRepository.findByPackageId(9L);
			case "Pymes": return paymentTypeRepository.findByPackageId(10L);
			case "Crecimiento": return paymentTypeRepository.findByPackageId(11L);
			case "Empresarial": return paymentTypeRepository.findByPackageId(12L);
			case "Plan S": return paymentTypeRepository.findByPackageId(13L);
			case "Plan M": return paymentTypeRepository.findByPackageId(14L);
			case "Plan L": return paymentTypeRepository.findByPackageId(15L);
			default: return null;
		}
	}


}
