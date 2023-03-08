package com.jtc.app.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jtc.app.primary.dao.BillProductRepository;
import com.jtc.app.primary.dao.BillRepository;
import com.jtc.app.primary.dao.BranchRepository;
import com.jtc.app.primary.dao.ContractFERepository;
import com.jtc.app.primary.dao.InvoiceRepository;
import com.jtc.app.primary.dao.InvoiceResumeRepository;
import com.jtc.app.primary.dao.PaymentTypeRepository;
import com.jtc.app.primary.dao.ProductRepository;
import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.BillDetail;
import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.ContractFE;
import com.jtc.app.primary.entity.FaceldiReportRow;
import com.jtc.app.primary.entity.InvoiceResume;
import com.jtc.app.primary.entity.JsonResponse;
import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.primary.entity.SmartReportRow;
import com.jtc.app.service.BillDetailService;
import com.jtc.app.service.BillService;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private InvoiceResumeRepository invoiceResumeRepository;
	@Autowired
	private ContractFERepository contractFeRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BillProductRepository billProductRepository;
	@Autowired
	private BranchRepository branchRepository;
	@Autowired
	private BillDetailService billDetailService;
	@Autowired
	private PaymentTypeRepository paymentTypeRepository;

	private Long initialInvoiceNumber = 0L;
	private Long lastInvoiceNumber = 0L;
	private List<BillProduct> tempBillProducts;
	private Object[] tempSmartFile;
	private Double[][] ipcList = { { 2018.0, 0.0 }, { 2019.0, 0.0380 }, { 2020.0, 0.0161 }, { 2021.0, 0.0562 },
			{ 2022.0, 0.1312 }, { 2023.0, 0.0 } };

	public Double calculateIpcIncrease(ContractFE contract, Long billingYear) {
		Double increase = 1.0;
		if (contract.getIpcIncrease()) {
			int contractYear = contract.getContractDate().getYear() + 1900;
			int i = 0;
			while (i < ipcList.length) {
				if (billingYear >= ipcList[i][0]) {
					if (contractYear < ipcList[i][0]) {
						increase *= (1 + ipcList[i - 1][1]);
					}
				}
				i++;
			}
		} else {
			int contractYear = contract.getContractDate().getYear() + 1900;
			int i = 4;
			while (i < ipcList.length) {
				if (billingYear >= ipcList[i][0]) {
					if (contractYear < ipcList[i][0]) {
						increase *= (1 + ipcList[i - 1][1]);
					}
				}
				i++;
			}
		}
		return Math.round(increase * Math.pow(10, 4)) / Math.pow(10, 4);
	}

	@Override
	public Bill saveBill(Bill bill) throws Exception {
		return billRepository.save(bill);
	}

	@Override
	public List<Bill> getBills() {
		return billRepository.findAll();
	}

	@Override
	public void deleteBill(Long billId) {
		billRepository.deleteById(billId);
	}

	@Override
	public Bill getBillById(Long billId) {
		return billRepository.getById(billId);
	}

	@Override
	public List<Bill> getFaceldiReport(Long year, Long month) {
		return billRepository.getFaceldiReport(year, month);
	}

	@Override
	public JsonResponse generateBills(Long year, Long month, Long invoiceNumber, String environment) {
		this.initialInvoiceNumber = invoiceNumber;
		this.lastInvoiceNumber = initialInvoiceNumber;
		Boolean update = environment.equalsIgnoreCase("S") ? false : true;
		LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap = new LinkedHashMap<>();
		List<InvoiceResume> resumes = invoiceResumeRepository.findByYearMonth(year, month).stream()
				.filter(resume -> resume.getBranch().getActive()).collect(Collectors.toList()); // Conteos de ese mes
		adjustContracts(year, month);
		getBillsToCollectByMonthlyIssuedInvoices(year, month, invoiceDetailFullMap, resumes, update);
		getBillsToCollectByOtherCharges(year, month, invoiceDetailFullMap, update);
		getBillsToCollectDueSharedContracts(year, month, invoiceDetailFullMap, resumes, update);
		saveBills(year, month, invoiceDetailFullMap, update);
		if (update) {
			try {
				BillDetail billDetail = new BillDetail();
				billDetail.setYear(year);
				billDetail.setMonth(month);
				billDetail.setInitialInvoice(initialInvoiceNumber);
				billDetail.setFinalInvoice(lastInvoiceNumber - 1);
				billDetailService.saveBillDetail(billDetail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.initialInvoiceNumber = 0L;
		this.lastInvoiceNumber = initialInvoiceNumber;
		return new JsonResponse("Se generó correctamente la facturación del periodo " + year + "-" + month);
	}

	@Override
	public Object[] generateTestBills(Long year, Long month, Long invoiceNumber) {
		this.initialInvoiceNumber = invoiceNumber;
		this.lastInvoiceNumber = initialInvoiceNumber;
		LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap = new LinkedHashMap<>();
		List<InvoiceResume> resumes = invoiceResumeRepository.findByYearMonth(year, month).stream()
				.filter(resume -> resume.getBranch().getActive()).collect(Collectors.toList()); // Conteos de ese mes
		adjustContracts(year, month);
		getBillsToCollectByMonthlyIssuedInvoices(year, month, invoiceDetailFullMap, resumes, false);
		getBillsToCollectByOtherCharges(year, month, invoiceDetailFullMap, false);
		getBillsToCollectDueSharedContracts(year, month, invoiceDetailFullMap, resumes, false);
		saveBills(year, month, invoiceDetailFullMap, false);
		Object[] result = generateFaceldiFile(year, month, "S");
		this.tempSmartFile = generateSmartFile(year, month, "S");

		this.initialInvoiceNumber = 0L;
		this.lastInvoiceNumber = initialInvoiceNumber;
		return result;
	}

	@Override
	public Object[] getSmartFile() {
		return this.tempSmartFile;
	}

	public void adjustContracts(Long year, Long month) {
		System.out.println("Running adjustContracts");
		List<ContractFE> contracts = contractFeRepository.findAll().stream()
				.filter(contract -> !contract.getSharedContract() && contract.getPaymentPlan().getSelfAdjusting())
				.collect(Collectors.toList());
		contracts.forEach(contract -> {
			System.out.println(contract.getContractId());
			PaymentType tempPlan = contract.getPaymentPlan();
			InvoiceResume tempResume = invoiceResumeRepository.findByBranchYearMonth(contract.getBranch().getBranchId(),
					year, month);
			Long issuedInvoices = 0L;
			if (tempResume != null) {
				issuedInvoices = tempResume.getIssuedInvoices();
			}
			String newPlanName = tempPlan.getJsonPackageName(issuedInvoices.intValue());
			if (!tempPlan.getPackageName().equalsIgnoreCase(newPlanName)) {
				switch (tempPlan.getPackageName()) {
				case "Plan S":
					if (newPlanName.equalsIgnoreCase("Plan M")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(14L));
					} else if (newPlanName.equalsIgnoreCase("Plan L")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(15L));
					}
					contractFeRepository.save(contract);
					break;
				case "Plan M":
					if (newPlanName.equalsIgnoreCase("Plan L")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(15L));
					}
					contractFeRepository.save(contract);
					break;
				// case "Plan L": When clients are in higher plans it cannot be a downgrade
				default:
				}
			}
		});
	}

	public void getBillsToCollectByMonthlyIssuedInvoices(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap, List<InvoiceResume> resumes,
			Boolean update) {
		System.out.println("Running getBillsToCollectByMonthlyIssuedInvoices");
		// Obtener los contratos compartidos
		List<String> sharedContractsFilter = contractFeRepository.getSharedContracts();
		// Obtener los contratos de esas sucursales
		for (InvoiceResume resume : resumes) {
			LinkedHashMap<Long, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			// Calcular el costo
			ContractFE contract = contractFeRepository.findByBranchId(resume.getBranch().getBranchId());

			if (contract != null) {
				// System.out.println(contract.getContractId());
				boolean skip = Optional.ofNullable(sharedContractsFilter.contains(contract.getContractId()))
						.orElse(false);
				if (skip == false) {
					// ---------------------------------------
					PaymentType tempPlan = contract.getPaymentPlan();
					Date contractDate = contract.getReferencePaymentDate();
					if (!contract.getSharedContract()) {
						if (tempPlan.getDiscriminatorType() == 1
								&& tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
							boolean chargeSubscription = false;
							Calendar calendar = Calendar.getInstance();
							Calendar dinamycCalendar = Calendar.getInstance();
							calendar.set(year.intValue(), month.intValue() - 1, 1, 0, 0, 0);
							dinamycCalendar.set(contractDate.getYear() + 1900, contractDate.getMonth(),
									contractDate.getDate(), 23, 59, 59);
							// int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
							// Calcula vencimiento de la anualidad
							if (calendar.get(Calendar.YEAR) > dinamycCalendar.get(Calendar.YEAR)) {
								if (calendar.get(Calendar.MONTH) <= dinamycCalendar.get(Calendar.MONTH)) {
									dinamycCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
								} else {
									dinamycCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
								}
							}

							if ((calendar.get(Calendar.MONTH)) == (dinamycCalendar.get(Calendar.MONTH))
									&& calendar.get(Calendar.YEAR) > dinamycCalendar.get(Calendar.YEAR)) {
								chargeSubscription = true;
							}

							// TODO Calcular las facturas emitidas durante el periodo del contrato, y si
							// aplica, generar cobro
							if (chargeSubscription) {
								calendar.set(year.intValue(), month.intValue() - 1, dinamycCalendar.get(Calendar.DATE));
								// calendar.add(Calendar.DATE, -1);
							} else {
								// calendar.set(year.intValue(), month.intValue() - 1, lastDayOfMonth);
								calendar.set(year.intValue(), month.intValue(), 1);
							}

							Long issuedDocs = invoiceRepository.getIssuedInvoicesDuringContract(
									resume.getBranch().getBranchId(), dinamycCalendar.getTime(), calendar.getTime());
							// Calcular las facturas emitidas del mes anterior

							/*
							 * calendar.add(Calendar.MONTH, -1); calendar.set(Calendar.DATE,
							 * calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
							 */

							Boolean refill = false;
							if (issuedDocs > tempPlan.getDocumentQuantity()) {
								//Validate if it is a Centsys' company to refill the contract
								Alliance alliance = contract.getBranch().getClient().getAlliance();
								if (alliance != null) {
									Long allianceId = alliance.getAllianceId();
									if (allianceId == 4L) {
										refill = true;
										chargeSubscription = true;
										
										//------------------------------------------------------
										Calendar endReferenceCalendar = Calendar.getInstance();
										endReferenceCalendar.setTime(calendar.getTime());
										endReferenceCalendar.add(Calendar.DATE, -1);
										Calendar startReferenceCalendar = Calendar.getInstance();
										startReferenceCalendar.setTime(calendar.getTime());
										startReferenceCalendar.set(Calendar.DATE, 1);
										
										for (Date date = startReferenceCalendar.getTime(); 
												startReferenceCalendar.before(endReferenceCalendar);
												startReferenceCalendar.add(Calendar.DATE, 1), date = startReferenceCalendar.getTime()) {
										    System.out.println(date);
										    Long tempDocsCount = invoiceRepository.getIssuedInvoicesDuringContract(
													resume.getBranch().getBranchId(), dinamycCalendar.getTime(), startReferenceCalendar.getTime());
										    if (tempDocsCount > tempPlan.getDocumentQuantity()) {
										    	System.out.println("Refill: " + contract.getContractId() + " - " 
										    + tempDocsCount + " - " + startReferenceCalendar.getTime());
										    	contract.setReferencePaymentDate(startReferenceCalendar.getTime());
										    	endReferenceCalendar.setTime(startReferenceCalendar.getTime());
										    }
										}
										//------------------------------------------------------
									}
								}
								
							}
							calendar.set(year.intValue(), month.intValue() - 1, 1);
							Long issuedDocsLastMonth = invoiceRepository.getIssuedInvoicesDuringContract(
									resume.getBranch().getBranchId(), dinamycCalendar.getTime(), calendar.getTime());
							
							invoiceDetailMap = tempPlan.getAnnualBillDetail(issuedDocs, issuedDocsLastMonth,
									chargeSubscription, refill);
						} else {
							invoiceDetailMap = tempPlan.getBillDetail(resume.getIssuedInvoices());
						}
					}
					addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract, invoiceDetailMap,
							invoiceDetailFullMap, update);
					if (update) {
						try {
							contractFeRepository.save(contract);
						} catch (Exception e) {
							System.out.println("Hubo un error al actualizar el contrato " + contract.getContractId());
						}						
					}
				} else {
					System.out.println(
							"La sucursal " + resume.getBranch().getBranchId() + " tiene un contrato compartido.");
				}
			} else {
				System.out.println("La sucursal " + resume.getBranch().getBranchId() + " no tiene contrato.");
			}
		}
	}

	public void getBillsToCollectByOtherCharges(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap, Boolean update) {
		System.out.println("Running getBillsToCollectByOtherCharges");

		List<ContractFE> contracts = contractFeRepository.findAll();
		Set<Long> keys = invoiceDetailFullMap.keySet();

		List<String> sharedContractsFilter = contractFeRepository.getSharedContracts();

		List<ContractFE> contractsFiltered = contracts.stream()
				.filter(c -> !keys.contains(c.getBranch().getBranchId()) && c.getBranch().getActive()
						&& !c.getSharedMaintenance() && !sharedContractsFilter.contains(c.getContractId()))
				.collect(Collectors.toList());

		contractsFiltered.forEach(contract -> {
			LinkedHashMap<Long, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			if (!contract.getSharedContract()) {
				Long[] valQuant = new Long[2];
				Date contractDate = contract.getReferencePaymentDate();
				PaymentType tempPlan = contract.getPaymentPlan();
				if (tempPlan.getDiscriminatorType() == 1) {
					if (tempPlan.getPaymentFrequency().getFrequencyId() == 4) {
						valQuant[0] = 1L;
						valQuant[1] = tempPlan.getPackagePrice();
						invoiceDetailMap.put(192L, valQuant);
					} else if (tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
						if (contractDate.getMonth() == month - 1 && (contractDate.getYear() + 1900) < year) {
							valQuant[0] = 1L;
							valQuant[1] = tempPlan.getPackagePrice();
							invoiceDetailMap.put(194L, valQuant);
							
							//--Fix date
							Calendar calendar = Calendar.getInstance();
							calendar.set(year.intValue(), month.intValue() - 1, contractDate.getDate());
							contract.setReferencePaymentDate(calendar.getTime());
							System.out.println("Anualidad " + contract.getContractId() + " - " + calendar.getTime());
							
							if (update) {
								try {
									contractFeRepository.save(contract);
								} catch (Exception e) {
									System.out.println("Hubo un error al actualizar el contrato " + contract.getContractId());
								}
							}
						}
					}
				} else if (tempPlan.getDiscriminatorType() == 2 && tempPlan.getMixedContract()) {
					valQuant[0] = 1L;
					valQuant[1] = tempPlan.getPackagePrice();
					invoiceDetailMap.put(192L, valQuant);
				}
			}

			addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract, invoiceDetailMap,
					invoiceDetailFullMap, update);
		});
	}

	public void getBillsToCollectDueSharedContracts(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap, List<InvoiceResume> resumes,
			Boolean update) {
		System.out.println("Running getBillsToCollectDueSharedContracts");
		List<ContractFE> contracts = contractFeRepository.findAll();
		List<String> sharedContracts = contractFeRepository.getSharedContracts();
		List<ContractFE> filteredContracts = contracts.stream()
				.filter(contract -> sharedContracts.contains(contract.getContractId())).collect(Collectors.toList());

		List<ContractFE> contractConsumers = contracts.stream().filter(contract -> contract.getSharedContract())
				.collect(Collectors.toList());

		for (ContractFE contract : filteredContracts) {
			LinkedHashMap<Long, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			PaymentType tempPlan = contract.getPaymentPlan();
			Date contractDate = contract.getReferencePaymentDate();

			List<ContractFE> tempContracts = contractConsumers.stream()
					.filter(c -> c.getSharedContractId().equalsIgnoreCase(contract.getContractId()))
					.collect(Collectors.toList());
			if (tempContracts != null) {
				List<Long> tempBranches = new ArrayList<>();
				tempBranches.add(contract.getBranch().getBranchId());
				tempContracts.forEach(c -> {
					tempBranches.add(c.getBranch().getBranchId());
				});
				Long issuedInvoices = 0L;
				if (tempPlan.getDiscriminatorType() == 1 && tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
					// Sumar las emitidas de ese año para cada una de las empresas que comparten el
					// contrato
					boolean chargeSubscription = false;
					Calendar calendar = Calendar.getInstance();
					Calendar dinamycCalendar = Calendar.getInstance();
					calendar.set(year.intValue(), month.intValue() - 1, 1, 0, 0, 0);
					dinamycCalendar.set(contractDate.getYear() + 1900, contractDate.getMonth(), contractDate.getDate(),
							23, 59, 59);
					// int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					// Calcula vencimiento de la anualidad
					if (calendar.get(Calendar.YEAR) > dinamycCalendar.get(Calendar.YEAR)) {
						if (calendar.get(Calendar.MONTH) <= dinamycCalendar.get(Calendar.MONTH)) {
							dinamycCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
						} else {
							dinamycCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
						}
					}

					if ((calendar.get(Calendar.MONTH)) == (dinamycCalendar.get(Calendar.MONTH))
							&& calendar.get(Calendar.YEAR) > dinamycCalendar.get(Calendar.YEAR)) {
						chargeSubscription = true;
					}

					// TODO Calcular las facturas emitidas durante el periodo del contrato, y si
					// aplica, generar cobro
					if (chargeSubscription) {
						calendar.set(year.intValue(), month.intValue() - 1, dinamycCalendar.get(Calendar.DATE));
						// calendar.add(Calendar.DATE, -1);
					} else {
						// calendar.set(year.intValue(), month.intValue() - 1, lastDayOfMonth);
						calendar.set(year.intValue(), month.intValue(), 1);
					}

					for (Long branchId : tempBranches) {
						issuedInvoices += invoiceRepository.getIssuedInvoicesDuringContract(branchId,
								dinamycCalendar.getTime(), calendar.getTime());
					}
					// Calcular las facturas emitidas del mes anterior

					/*
					 * calendar.add(Calendar.MONTH, -1); calendar.set(Calendar.DATE,
					 * calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					 */

					Boolean refill = false;
					if (issuedInvoices > tempPlan.getDocumentQuantity()) {
						//Validate if it is a Centsys' company to refill the contract
						Alliance alliance = contract.getBranch().getClient().getAlliance();
						if (alliance != null) {
							Long allianceId = alliance.getAllianceId();
							if (allianceId == 4L) {
								refill = true;
								chargeSubscription = true;
								
								//------------------------------------------------------
								Calendar endReferenceCalendar = Calendar.getInstance();
								endReferenceCalendar.setTime(calendar.getTime());
								endReferenceCalendar.add(Calendar.DATE, -1);
								Calendar startReferenceCalendar = Calendar.getInstance();
								startReferenceCalendar.setTime(calendar.getTime());
								startReferenceCalendar.set(Calendar.DATE, 1);
								
								for (Date date = startReferenceCalendar.getTime(); 
										startReferenceCalendar.before(endReferenceCalendar);
										startReferenceCalendar.add(Calendar.DATE, 1), date = startReferenceCalendar.getTime()) {
								    System.out.println(date);
								    Long tempDocsCount = 0L;
								    for (Long branchId : tempBranches) {
								    	tempDocsCount += invoiceRepository.getIssuedInvoicesDuringContract(branchId,
												dinamycCalendar.getTime(), startReferenceCalendar.getTime());
									}
								    
								    if (tempDocsCount > tempPlan.getDocumentQuantity()) {
								    	System.out.println("Refill: " + contract.getContractId() + " - " 
											    + tempDocsCount + " - " + startReferenceCalendar.getTime());
								    	contract.setReferencePaymentDate(startReferenceCalendar.getTime());
								    	endReferenceCalendar.setTime(startReferenceCalendar.getTime());
								    }
								}
								//------------------------------------------------------
							}
						}
						
					}
					calendar.set(year.intValue(), month.intValue() - 1, 1);
					
					Long issuedDocsLastMonth = 0L;
					for (Long branchId : tempBranches) {
						issuedDocsLastMonth += invoiceRepository.getIssuedInvoicesDuringContract(branchId,
								dinamycCalendar.getTime(), calendar.getTime());
					}
					
					invoiceDetailMap = tempPlan.getAnnualBillDetail(issuedInvoices, issuedDocsLastMonth,
							chargeSubscription, refill);
				} else {
					// Sumar las emitidas de ese mes para cada una de las empresas que comparten el
					// contrato
					for (InvoiceResume resume : resumes) {
						if (tempBranches.contains(resume.getBranch().getBranchId())) {
							issuedInvoices += resume.getIssuedInvoices();
						}
					}
					invoiceDetailMap = tempPlan.getBillDetail(issuedInvoices);
				}

			}
			
			if (update) {
				try {
					contractFeRepository.save(contract);
				} catch (Exception e) {
					System.out.println("Hubo un error al actualizar el contrato " + contract.getContractId());
				}
			}
			
			addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract, invoiceDetailMap,
					invoiceDetailFullMap, update);
		}

	}

	public void addMoreDetailsToPay(Long year, Long month, Long branchId, ContractFE contract,
			LinkedHashMap<Long, Long[]> invoiceDetailMap,
			LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap, Boolean update) {
		Alliance alliance = contract.getBranch().getClient().getAlliance();
		Date contractDate = contract.getContractDate();
		
		if (alliance != null) {
			if (alliance.getAllianceId() == 4L) {
				contractDate = Optional.ofNullable(contract.getFirstIssueDate()).orElse(contractDate);
			}
		}

		// Cobro de implementacion pendiente
		if (contract.getImplementationAlreadyPaid() == false) {
			Long[] valueXQuantity = { 1L, contract.getImplementationCost() };
			invoiceDetailMap.put(190L, valueXQuantity);
			contract.setImplementationAlreadyPaid(true);
			if (update) {
				try {
					contractFeRepository.save(contract);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Cobro de mantenimiento vencido de ese periodo
		if (!contract.getSharedMaintenance()) {
			if (contractDate.getYear() + 1900 < year) {
				if (contractDate.getMonth() + 1 == month) {
					Long[] valueXQuantity = { 1L, contract.getMaintenanceType().getMaintenanceCost() };
					invoiceDetailMap.put(196L, valueXQuantity);
				}
			}

			// Cobro de mantenimiento vencido de periodos pasados
			if (contract.getMaintenanceAlreadyPaid() == false) {
				Long[] valueXQuantity = { 1L, contract.getMaintenanceType().getMaintenanceCost() };
				invoiceDetailMap.put(200L, valueXQuantity);
				contract.setMaintenanceAlreadyPaid(true);
				if (update) {
					try {
						contractFeRepository.save(contract);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (!invoiceDetailMap.isEmpty()) {
			invoiceDetailFullMap.put(branchId, invoiceDetailMap);
		}
	}

	public void saveBills(Long year, Long month, LinkedHashMap<Long, LinkedHashMap<Long, Long[]>> invoiceDetailFullMap,
			Boolean save) {
		System.out.println("Running saveBills");
		this.tempBillProducts = new ArrayList<>();
		Calendar calendar = null;
		Bill bill = null;
		ContractFE tempContract = null;
		BillProduct billProduct = null;
		for (Map.Entry<Long, LinkedHashMap<Long, Long[]>> invoiceDetailMap : invoiceDetailFullMap.entrySet()) {
			calendar = Calendar.getInstance();
			cleanDetailMap(invoiceDetailMap.getValue());
			if (!invoiceDetailMap.getValue().isEmpty()) {
				bill = new Bill();
				tempContract = contractFeRepository.findByBranchId(invoiceDetailMap.getKey());
				bill.setBillNumber(this.lastInvoiceNumber);
				bill.setCreationDate(calendar.getTime());
				calendar.add(Calendar.DATE, 30);
				bill.setExpirationDate(calendar.getTime());
				bill.setYear(year);
				bill.setMonth(month);
				bill.setBranch(branchRepository.findByBranchId(invoiceDetailMap.getKey()));
				bill.setDescription(generateInvoiceDescription(year, month, tempContract, invoiceDetailMap.getValue()));
				Double ponderedIpc = this.calculateIpcIncrease(tempContract, year);
				Double ipcUntilLastYear = this.calculateIpcIncrease(tempContract, year - 1);
				if (save) {
					try {
						billRepository.save(bill);
						System.out.println("Saved " + bill.getBillId() + ", " + bill.getDescription());
						InvoiceResume row = invoiceResumeRepository.findByBranchYearMonth(invoiceDetailMap.getKey(), year,
								month);
						if (row != null) {
							row.setBill(bill);
							invoiceResumeRepository.save(row);
						}
						this.lastInvoiceNumber++;
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					for (Map.Entry<Long, Long[]> concept : invoiceDetailMap.getValue().entrySet()) {
						billProduct = new BillProduct();
						billProduct.setBill(bill);
						billProduct.setProduct(productRepository.findByProductId(concept.getKey()));
						billProduct.setQuantity(concept.getValue()[0].longValue());
						billProduct.setPrice(concept.getKey() == 190L ? concept.getValue()[1]
								: concept.getKey() == 200L
										? new Double(concept.getValue()[1] * ipcUntilLastYear).longValue()
										: new Double(concept.getValue()[1] * ponderedIpc).longValue());
						try {
							billProductRepository.save(billProduct);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					for (Map.Entry<Long, Long[]> concept : invoiceDetailMap.getValue().entrySet()) {
						billProduct = new BillProduct();
						billProduct.setBill(bill);
						billProduct.setProduct(productRepository.findByProductId(concept.getKey()));
						billProduct.setQuantity(concept.getValue()[0].longValue());
						billProduct.setPrice(concept.getKey() == 190L ? concept.getValue()[1]
								: concept.getKey() == 200L
										? new Double(concept.getValue()[1] * ipcUntilLastYear).longValue()
										: new Double(concept.getValue()[1] * ponderedIpc).longValue());
						try {
							tempBillProducts.add(billProduct);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					this.lastInvoiceNumber++;
				}
			}
		}
	}

	public LinkedHashMap<Long, Long[]> cleanDetailMap(LinkedHashMap<Long, Long[]> invoiceDetailMap) {
		List<Long> keysToRemove = new ArrayList<>();
		invoiceDetailMap.forEach((k, v) -> {
			if (v[0] == 0 || v[1] == 0) {
				keysToRemove.add(k);
			}
		});
		if (!keysToRemove.isEmpty()) {
			keysToRemove.stream().forEach(key -> {
				invoiceDetailMap.remove(key);
			});
		}
		return invoiceDetailMap;
	}

	public String generateInvoiceDescription(Long year, Long month, ContractFE contract,
			LinkedHashMap<Long, Long[]> invoiceDetailMap) {
		cleanDetailMap(invoiceDetailMap);
		String description = "";
		Set<Long> keys = invoiceDetailMap.keySet();
		String monthString = getMonthFromNumber(month);
		
		if (keys.contains(194L)) {
			description = "Facturación anual " + monthString + " " + year + " - " + (year + 1) + ". ";
		} else {
			description = "Facturación correspondiente al mes de " + monthString + " " + year + ". ";
		}
		
		if (keys.contains(196L)) {
			description += "Mantenimiento Facturación Electrónica entre " + monthString + " " + year + "-" + 
		(year + 1) + " sucursal \"" + contract.getBranch().getCode() + " - " + contract.getBranch().getName() + "\". ";
		}
		
		if (keys.contains(200L)) {
			description += "Mantenimiento Facturación Electrónica periodos anteriores vencidos sucursal \""
					+ contract.getBranch().getCode() + " - " + contract.getBranch().getName() + "\". ";
		}
		
		return description;
	}

	/*public String generateBaseDescription(Long year, Long month, Long product, ContractFE contract) {
		String monthString = getMonthFromNumber(month);
		String branchName = contract.getBranch().getCode() + " - " + contract.getBranch().getName();
		String description = "";
		switch (product.intValue()) {
			case 190:
				description = "Cobro de implementación Facturación electrónica sucursal \"" + branchName + "\". ";
				break;
			case 194:
				description = "Anualidad " + monthString + " " + year + " - " + (year + 1) + ". ";
				break;
			case 196:
				description = "Mantenimiento Facturación Electrónica entre " + monthString + "-" + year + " y "
						+ monthString + "-" + (year + 1) + " sucursal \"" + branchName + "\". ";
				break;
			case 200:
				description = "Mantenimiento Facturación Electrónica periodos anteriores vencidos sucursal \""
						+ branchName + "\". ";
				break;
			default:
		}
		return description;
	}*/

	public String getMonthFromNumber(Long month) {
		switch (month.intValue()) {
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Septiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		default:
			return "";
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class, transactionManager = "primaryTransactionManager")
	public Object[] generateFaceldiFile(Long year, Long month, String simulation) {
		System.out.println("Running generateFaceldiFile");
		List<String> rowLines = new ArrayList<>();
		if (!simulation.equalsIgnoreCase("S")) {
			List<Bill> bills = billRepository.getFaceldiReport(year, month);
			bills.forEach(bill -> {
				Branch branch = bill.getBranch();
				ContractFE contract = contractFeRepository.findByBranchId(bill.getBranch().getBranchId());
				List<BillProduct> billProducts = billProductRepository.getByBillId(bill.getBillId());
				billProducts.forEach(billProduct -> {
					FaceldiReportRow row = new FaceldiReportRow();
					row.setFcConsecutivo(bill.getBillNumber());
					row.setFcFechaDocumento(bill.getCreationDate());
					row.setFcFechaVencimiento(bill.getExpirationDate());
					row.setFcObservacionDocumento(bill.getDescription());
					row.setFcFechaTasaCambio(bill.getCreationDate());
					row.setFcFechaInicioPeriodoFacturacion(bill.getCreationDate());
					row.setFcFechaFinPeriodoFacturacion(bill.getExpirationDate());
					row.setFcReferenciaOrdenCompra("Contrato #" + contract.getContractId());
					row.setClTipoOrganizacion(contract.getBranch().getClient().getTipoPersona());
					row.setClTipoRegimen(contract.getBranch().getClient().getTipoRegimen());
					row.setClNombreComercial(branch.getClient().getRazonSocial());
					row.setClCodigoDepMunicipio(
							branch.getState().toString().length() == 4 ? "0" + branch.getState().toString()
									: branch.getState().toString());
					row.setClCodigoPostal(branch.getClient().getZonaPostal());
					row.setClDireccion(branch.getAddress());
					row.setClNombreLegal(branch.getClient().getRazonSocial());
					row.setClTipoIdAdquiriente(contract.getBranch().getClient().getTipoIdentificacion());
					row.setClNumeroIdAdquiriente(branch.getClient().getNit());
					row.setClCodigoMunicipio(
							branch.getState().toString().length() == 4 ? "0" + branch.getState().toString()
									: branch.getState().toString());
					row.setClCodigoPostal2(branch.getClient().getZonaPostal());
					row.setClDireccion2(branch.getAddress());
					row.setClTelefono(branch.getPhone());
					row.setClCelular(branch.getPhone());
					row.setClCorreo((billProduct.getBill().getBranch().getClient().getCorreoElectronico()
							+ ", facelectronica_cnt@jaimetorres.net").replace(",,", ","));
					row.setPrCodigoProducto(billProduct.getProduct().getProductId() == 200L ? 196L
							: billProduct.getProduct().getProductId());
					row.setPrDescripcion(
							billProduct.getProduct().getProductId() == 200L ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
									: billProduct.getProduct().getDescription());
					row.setPrDescripcionAdicional(bill.getDescription());
					row.setPrPrecioUnitario(billProduct.getPrice());
					row.setPrCantidad(billProduct.getQuantity());
					row.setPrBaseGravable(row.getPrPrecioUnitario() * row.getPrCantidad());
					if (!(row.getPrPrecioUnitario() == 0 || row.getPrCantidad() == 0)) {
						rowLines.add(row.toString());
					}

				});
			});
		} else {
			this.tempBillProducts.forEach(billProduct -> {
				FaceldiReportRow row = new FaceldiReportRow();
				ContractFE contract = contractFeRepository.findByBranchId(billProduct.getBill().getBranch().getBranchId());
				row.setFcConsecutivo(billProduct.getBill().getBillNumber());
				row.setFcFechaDocumento(billProduct.getBill().getCreationDate());
				row.setFcFechaVencimiento(billProduct.getBill().getExpirationDate());
				row.setFcObservacionDocumento(billProduct.getBill().getDescription());
				row.setFcFechaTasaCambio(billProduct.getBill().getCreationDate());
				row.setFcFechaInicioPeriodoFacturacion(billProduct.getBill().getCreationDate());
				row.setFcFechaFinPeriodoFacturacion(billProduct.getBill().getExpirationDate());
				row.setFcReferenciaOrdenCompra("Contrato #" + contract.getContractId());
				row.setClTipoOrganizacion(contract.getBranch().getClient().getTipoPersona());
				row.setClTipoRegimen(contract.getBranch().getClient().getTipoRegimen());
				row.setClNombreComercial(billProduct.getBill().getBranch().getClient().getRazonSocial());
				row.setClCodigoDepMunicipio(billProduct.getBill().getBranch().getState().toString().length() == 4
						? "0" + billProduct.getBill().getBranch().getState().toString()
						: billProduct.getBill().getBranch().getState().toString());
				row.setClCodigoPostal(billProduct.getBill().getBranch().getClient().getZonaPostal());
				row.setClDireccion(billProduct.getBill().getBranch().getAddress());
				row.setClNombreLegal(billProduct.getBill().getBranch().getClient().getRazonSocial());
				row.setClTipoIdAdquiriente(contract.getBranch().getClient().getTipoIdentificacion());
				row.setClNumeroIdAdquiriente(billProduct.getBill().getBranch().getClient().getNit());
				row.setClCodigoMunicipio(billProduct.getBill().getBranch().getState().toString().length() == 4
						? "0" + billProduct.getBill().getBranch().getState().toString()
						: billProduct.getBill().getBranch().getState().toString());
				row.setClCodigoPostal2(billProduct.getBill().getBranch().getClient().getZonaPostal());
				row.setClDireccion2(billProduct.getBill().getBranch().getAddress());
				row.setClTelefono(billProduct.getBill().getBranch().getPhone());
				row.setClCelular(billProduct.getBill().getBranch().getPhone());
				row.setClCorreo((billProduct.getBill().getBranch().getClient().getCorreoElectronico()
						+ ", facelectronica_cnt@jaimetorres.net").replace(",,", ","));
				row.setPrCodigoProducto(billProduct.getProduct().getProductId() == 200L ? 196L
						: billProduct.getProduct().getProductId());
				row.setPrDescripcion(
						billProduct.getProduct().getProductId() == 200L ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
								: billProduct.getProduct().getDescription());
				row.setPrDescripcionAdicional(billProduct.getBill().getDescription());
				row.setPrPrecioUnitario(billProduct.getPrice());
				row.setPrCantidad(billProduct.getQuantity());
				row.setPrBaseGravable(row.getPrPrecioUnitario() * row.getPrCantidad());
				if (!(row.getPrPrecioUnitario() == 0 || row.getPrCantidad() == 0)) {
					rowLines.add(row.toString());
				}
			});
		}
		return rowLines.toArray();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class, transactionManager = "primaryTransactionManager")
	public Object[] generateSmartFile(Long year, Long month, String simulation) {
		System.out.println("Running generateSmartFile");
		List<String> rowLines = new ArrayList<>();
		if (!simulation.equalsIgnoreCase("S")) {
			List<Bill> bills = billRepository.getFaceldiReport(year, month);
			bills.forEach(bill -> {
				List<BillProduct> billProducts = billProductRepository.getByBillId(bill.getBillId());
				Long lastEvaluatedInvoice = 0L;
				Integer itemIndex = 1;
				for (BillProduct billProduct : billProducts) {
					if (lastEvaluatedInvoice == billProduct.getBill().getBillId()) {
						if (!(billProduct.getPrice() == 0 || billProduct.getQuantity() == 0)) {
							itemIndex++;
						}
					} else {
						lastEvaluatedInvoice = billProduct.getBill().getBillId();
						itemIndex = 1;
					}
					SmartReportRow row = new SmartReportRow();
					Alliance alliance = billProduct.getBill().getBranch().getClient().getAlliance();
					row.setDocumentNo(billProduct.getBill().getBillNumber());
					row.setDescription(billProduct.getBill().getDescription());
					row.setDateInvoiced(billProduct.getBill().getCreationDate());
					row.setDateAcct(billProduct.getBill().getCreationDate());
					row.setCBPartnerId(billProduct.getBill().getBranch().getClient().getNit());
					row.setCActivityId(alliance != null ? alliance.getSmartCC() : 15L);
					row.setCInvoiceLineCInvoiceId(billProduct.getBill().getBillNumber());
					row.setCInvoiceLine(itemIndex);
					row.setCInvoiceLineMProductId(billProduct.getProduct().getProductId() == 200L ? 196L
							: billProduct.getProduct().getProductId());
					row.setCInvoiceLineDescription(row.getDescription() + " "
							+ (billProduct.getProduct().getProductId() == 200L
									? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
									: billProduct.getProduct().getDescription()));
					row.setCInvoiceLineQtyEntered(billProduct.getQuantity());
					row.setCInvoiceLinePriceEntered(billProduct.getPrice());
					row.setCInvoiceLinePriceList(billProduct.getPrice());
					row.setIdType(billProduct.getBill().getBranch().getClient().getTipoIdentificacion());
					if (!(row.getCInvoiceLinePriceEntered() == 0 || row.getCInvoiceLineQtyEntered() == 0)) {
						rowLines.add(row.toString());
					}
				}
				;
			});
		} else {
			Long lastEvaluatedInvoice = 0L;
			Integer itemIndex = 1;
			for (BillProduct billProduct : this.tempBillProducts) {
				if (lastEvaluatedInvoice == billProduct.getBill().getBillNumber()) {
					if (!(billProduct.getPrice() == 0 || billProduct.getQuantity() == 0)) {
						itemIndex++;
					}
				} else {
					lastEvaluatedInvoice = billProduct.getBill().getBillNumber();
					itemIndex = 1;
				}
				SmartReportRow row = new SmartReportRow();
				Alliance alliance = billProduct.getBill().getBranch().getClient().getAlliance();
				row.setDocumentNo(billProduct.getBill().getBillNumber());
				row.setDescription(billProduct.getBill().getDescription());
				row.setDateInvoiced(billProduct.getBill().getCreationDate());
				row.setDateAcct(billProduct.getBill().getCreationDate());
				row.setCBPartnerId(billProduct.getBill().getBranch().getClient().getNit());
				row.setCActivityId(alliance != null ? alliance.getSmartCC() : 15L);
				row.setCInvoiceLineCInvoiceId(billProduct.getBill().getBillNumber());
				row.setCInvoiceLine(itemIndex);
				row.setCInvoiceLineMProductId(billProduct.getProduct().getProductId() == 200L ? 196L
						: billProduct.getProduct().getProductId());
				row.setCInvoiceLineDescription(row.getDescription() + " "
						+ (billProduct.getProduct().getProductId() == 200L ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
								: billProduct.getProduct().getDescription()));
				row.setCInvoiceLineQtyEntered(billProduct.getQuantity());
				row.setCInvoiceLinePriceEntered(billProduct.getPrice());
				row.setCInvoiceLinePriceList(billProduct.getPrice());
				row.setIdType(billProduct.getBill().getBranch().getClient().getTipoIdentificacion());
				if (!(row.getCInvoiceLinePriceEntered() == 0 || row.getCInvoiceLineQtyEntered() == 0)) {
					rowLines.add(row.toString());
				}
			}
		}
		return rowLines.toArray();
	}

}
