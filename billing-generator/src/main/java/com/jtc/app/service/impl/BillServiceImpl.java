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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jtc.app.primary.dao.BillProductRepository;
import com.jtc.app.primary.dao.BillRepository;
import com.jtc.app.primary.dao.BranchRepository;
import com.jtc.app.primary.dao.CancellationRepository;
import com.jtc.app.primary.dao.ContractRepository;
import com.jtc.app.primary.dao.InvoiceRepository;
import com.jtc.app.primary.dao.InvoiceResumeRepository;
import com.jtc.app.primary.dao.PaymentTypeRepository;
import com.jtc.app.primary.dao.ProductRepository;
import com.jtc.app.primary.entity.Alliance;
import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.BillDetail;
import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.Contract;
import com.jtc.app.primary.entity.FaceldiReportRow;
import com.jtc.app.primary.entity.InvoiceResume;
import com.jtc.app.primary.entity.JsonResponse;
import com.jtc.app.primary.entity.PaymentType;
import com.jtc.app.primary.entity.SmartReportRow;
import com.jtc.app.service.BillDetailService;
import com.jtc.app.service.BillService;

@Service
@Configurable
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private InvoiceResumeRepository invoiceResumeRepository;
	@Autowired
	private ContractRepository contractRepository;
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
	@Autowired
	private CancellationRepository cancellationRepository;

	private Long initialInvoiceNumber = 0L;
	private Long lastInvoiceNumber = 0L;
	private List<BillProduct> tempBillProducts;
	private Object[] tempSmartFile;
	private Double[][] ipcList = { { 2018.0, 0.0 }, { 2019.0, 0.0380 }, { 2020.0, 0.0161 }, { 2021.0, 0.0562 },
			{ 2022.0, 0.1312 }, { 2023.0, 0.0 } };

	/**
	 * Permite calcular el porcentaje de incremento por IPC que tendrá un plan según la fecha de firma del contrato.
	 * @param contract (El contrato que será evaluado).
	 * @param billingYear (Año en el que se genera la facturación).
	 * @return El porcentaje de aumento de IPC.
	 */
	public Double calculateIpcIncrease(Contract contract, Long billingYear) {
		Double increase = 1.0;
		if (contract.getIpcIncrease() && !contract.getModule().equals("NE")) {
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
	public JsonResponse generateBills(Long year, Long month, Long invoiceNumber, String environment, String module) {
		this.initialInvoiceNumber = invoiceNumber;
		this.lastInvoiceNumber = initialInvoiceNumber;
		Boolean update = environment.equalsIgnoreCase("S") ? false : true;
		LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap = new LinkedHashMap<>();
		List<InvoiceResume> resumes = invoiceResumeRepository.findByYearMonthModule(year, month, module).stream()
				.filter(resume -> module.equals("FE") ? resume.getBranch().getFE()
						: module.equals("DS") ? resume.getBranch().getDS() : resume.getBranch().getNE())
				.collect(Collectors.toList()); // Conteos de ese mes
		if (module.equals("FE")) {
			adjustContracts(year, month);
		}
		getBillsToCollectByMonthlyIssuedInvoices(year, month, invoiceDetailFullMap, resumes, update, module);
		getBillsToCollectByOtherCharges(year, month, invoiceDetailFullMap, update, module);
		getBillsToCollectDueSharedContracts(year, month, invoiceDetailFullMap, resumes, update, module);
		saveBills(year, month, invoiceDetailFullMap, update, module);
		if (update) {
			try {
				BillDetail billDetail = new BillDetail();
				billDetail.setYear(year);
				billDetail.setMonth(month);
				billDetail.setInitialInvoice(initialInvoiceNumber);
				billDetail.setFinalInvoice(lastInvoiceNumber - 1);
				billDetail.setModule(module);
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
	public Object[] generateTestBills(Long year, Long month, Long invoiceNumber, String module) {
		this.initialInvoiceNumber = invoiceNumber;
		this.lastInvoiceNumber = initialInvoiceNumber;
		LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap = new LinkedHashMap<>();
		List<InvoiceResume> resumes = invoiceResumeRepository.findByYearMonthModule(year, month, module).stream()
				.filter(resume -> module.equals("FE") ? resume.getBranch().getFE()
						: module.equals("DS") ? resume.getBranch().getDS() : resume.getBranch().getNE())
				.collect(Collectors.toList()); // Conteos de ese mes
		if (module.equals("FE")) {
			adjustContracts(year, month);
		}
		getBillsToCollectByMonthlyIssuedInvoices(year, month, invoiceDetailFullMap, resumes, false, module);
		getBillsToCollectByOtherCharges(year, month, invoiceDetailFullMap, false, module);
		getBillsToCollectDueSharedContracts(year, month, invoiceDetailFullMap, resumes, false, module);
		saveBills(year, month, invoiceDetailFullMap, false, module);
		Object[] result = generateFaceldiFile(year, month, "S", module);
		this.tempSmartFile = generateSmartFile(year, month, "S", module);

		this.initialInvoiceNumber = 0L;
		this.lastInvoiceNumber = initialInvoiceNumber;
		return result;
	}

	@Override
	public Object[] getSmartFile() {
		return this.tempSmartFile;
	}

	/**
	 * Ajusta el plan de pago si es de tipo autoajustable en la escala de Plan M, Plan L y Plan XL de FE;
	 * lo anterior, según la cantidad de documentos emitidos en el mes. 
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 */
	public void adjustContracts(Long year, Long month) {
		System.out.println("Running adjustContracts - FE");
		List<Contract> contracts = contractRepository
				.findAll().stream().filter(contract -> !contract.getSharedContract()
						&& contract.getPaymentPlan().getSelfAdjusting() && contract.getBranch().getFE())
				.collect(Collectors.toList());
		contracts.forEach(contract -> {
			PaymentType tempPlan = contract.getPaymentPlan();
			InvoiceResume tempResume = invoiceResumeRepository.findByBranchYearMonth(contract.getBranch().getBranchId(),
					year, month, "FE");
			Long issuedInvoices = 0L;
			if (tempResume != null) {
				issuedInvoices = tempResume.getIssuedInvoices();
			}
			String newPlanName = tempPlan.getJsonPackageInfo(issuedInvoices.intValue()).get(0);
			if (!tempPlan.getPackageName().equalsIgnoreCase(newPlanName)) {
				switch (tempPlan.getPackageName()) {
				case "Plan S":
					if (newPlanName.equalsIgnoreCase("Plan M")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(14L));
					} else if (newPlanName.equalsIgnoreCase("Plan L")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(15L));
					}
					contractRepository.save(contract);
					break;
				case "Plan M":
					if (newPlanName.equalsIgnoreCase("Plan L")) {
						contract.setPaymentPlan(paymentTypeRepository.findByPackageId(15L));
					}
					contractRepository.save(contract);
					break;
				// case "Plan L": When clients are in higher plans it cannot be a downgrade
				default:
				}
			}
		});
	}

	/**
	 * Evalua todas las empresas que emitieron documentos en el mes para determinar el cobro que se les debe realizar.
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param invoiceDetailFullMap (Variable en la que se guarda el detalle de todas las facturas que se van a generar).
	 * @param resumes (Listado de conteos de facturasa correspondientes al periodo).
	 * @param update (true si los cambios se deben guardar en la base de datos de la aplicación. En su defecto, false).
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 */
	public void getBillsToCollectByMonthlyIssuedInvoices(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap, List<InvoiceResume> resumes,
			Boolean update, String module) {
		System.out.println("Running getBillsToCollectByMonthlyIssuedInvoices - " + module);
		// Obtener los contratos compartidos
		List<String> sharedContractsFilter = contractRepository.getSharedContracts(module);

		// Obtener los contratos de esas sucursales
		for (InvoiceResume resume : resumes) {
			LinkedHashMap<String, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			// Calcular el costo

			Contract contract = contractRepository.findByBranchId(resume.getBranch().getBranchId(), module);

			if (contract != null) {
				System.out.println(contract.getContractId());
				if (cancellationRepository.findByContractId(contract.getContractId()) == null) {
					boolean skip = Optional.ofNullable(sharedContractsFilter.contains(contract.getContractId()))
							.orElse(false);
					if (!skip) {
						// ---------------------------------------
						PaymentType tempPlan = contract.getPaymentPlan();
						Date contractDate = contract.getReferencePaymentDate();
						if (!contract.getSharedContract()) {
							if ((tempPlan.getDiscriminatorType() == 1 || tempPlan.getDiscriminatorType() == 5
									|| tempPlan.getDiscriminatorType() == 6)
									&& tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
								boolean chargeSubscription = false;
								Calendar calendar = Calendar.getInstance(); //Fecha actual de calculo de la facturación
								Calendar dinamycCalendar = Calendar.getInstance(); //Fecha de referencia del último cobro
								calendar.set(year.intValue(), month.intValue() - 1, 1, 0, 0, 0);
								dinamycCalendar.set(contractDate.getYear() + 1900, contractDate.getMonth(),
										contractDate.getDate(), 23, 59, 59);
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
									calendar.set(year.intValue(), month.intValue() - 1,
											dinamycCalendar.get(Calendar.DATE));
									contract.setReferencePaymentDate(calendar.getTime());
								} else {
									calendar.set(year.intValue(), month.intValue(), 1);
								}

								Long issuedDocs = module.equals("FE")
										? invoiceRepository.getFeIssuedInvoicesDuringContract(
												resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
												calendar.getTime())
										: module.equals("DS")
												? invoiceRepository.getDsIssuedInvoicesDuringContract(
														resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
														calendar.getTime())
												: invoiceRepository.getNeIssuedInvoicesDuringContract(
														resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
														calendar.getTime());
								// Calcular las facturas emitidas del mes anterior

								Boolean refill = false;
								if (issuedDocs > tempPlan.getDocumentQuantity()) {
									// Validate if it is a Centsys' company to refill the contract
									Alliance alliance = contract.getBranch().getClient().getAlliance();
									if (alliance != null || !module.equalsIgnoreCase("FE")) {
										Long allianceId = 0L;
										if (alliance != null) {
											allianceId = alliance.getAllianceId();
										}
										
										if (module.equals("FE") ? allianceId == 4L 
												: (module.equalsIgnoreCase("NE") || module.equalsIgnoreCase("DS"))) {
											refill = true;
											chargeSubscription = true;

											Calendar endReferenceCalendar = Calendar.getInstance();
											endReferenceCalendar.setTime(calendar.getTime());
											endReferenceCalendar.add(Calendar.DATE, 1);
											Calendar startReferenceCalendar = Calendar.getInstance();
											startReferenceCalendar.set(Calendar.MONTH, (int) (month - 1));
											startReferenceCalendar.set(Calendar.DATE, 1);

											for (Date date = startReferenceCalendar.getTime(); date
													.before(endReferenceCalendar.getTime()); startReferenceCalendar.add(
															Calendar.DATE,
															1), date = startReferenceCalendar.getTime()) {

												Long tempDocsCount = module.equals("FE")
														? invoiceRepository.getFeIssuedInvoicesDuringContract(
																resume.getBranch().getBranchId(),
																dinamycCalendar.getTime(), date)
														: module.equals("DS")
																? invoiceRepository.getDsIssuedInvoicesDuringContract(
																		resume.getBranch().getBranchId(),
																		dinamycCalendar.getTime(), date)
																: invoiceRepository.getNeIssuedInvoicesDuringContract(
																		resume.getBranch().getBranchId(),
																		dinamycCalendar.getTime(), date);
												if (tempDocsCount > tempPlan.getDocumentQuantity()) {
													contract.setReferencePaymentDate(date);
													if (date.getMonth() > (int) (month - 1)) {
														startReferenceCalendar.add(Calendar.DATE, -1);
													}
													/*System.out.println("Refill: " + contract.getContractId() + " - "
															+ tempDocsCount + " - " + startReferenceCalendar.getTime());*/
													contract.setReferencePaymentDate(startReferenceCalendar.getTime());
													endReferenceCalendar.setTime(date);
												}
											}
										}
										
										if (module.equals("DS")) {
											String newPackageName = tempPlan.getJsonPackageInfo(issuedDocs.intValue()).get(0);
											Integer newPackageDocQuantity = Integer.parseInt(tempPlan.getJsonPackageInfo(issuedDocs.intValue()).get(2));
											Long newPackagePrice = Long.parseLong(tempPlan.getJsonPackageInfo(issuedDocs.intValue()).get(3));
											
											PaymentType newPlan = paymentTypeRepository
													.findPackageByParams(6, tempPlan.getCostRange(),
															newPackageName,	newPackageDocQuantity, newPackagePrice,
															0L, tempPlan.getPaymentFrequency(), module, false, true);
											
											if (newPlan == null) {
												newPlan = new PaymentType();
												newPlan.setDiscriminatorType(6);
												newPlan.setCostRange(tempPlan.getCostRange());
												newPlan.setPackageName(newPackageName);
												newPlan.setDocumentQuantity(newPackageDocQuantity);
												newPlan.setPackagePrice(newPackagePrice);
												newPlan.setDocumentPrice(0L);
												newPlan.setPaymentFrequency(tempPlan.getPaymentFrequency());
												newPlan.setModulePlan("DS");
												newPlan.setMixedContract(false);
												newPlan.setSelfAdjusting(true);
												
												newPlan = paymentTypeRepository.save(newPlan);
											}
											
											contract.setPaymentPlan(newPlan);
										}
									}
								}
								calendar.set(year.intValue(), month.intValue() - 1, 1);
								Long issuedDocsLastMonth = module.equals("FE")
										? invoiceRepository.getFeIssuedInvoicesDuringContract(
												resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
												calendar.getTime())
										: module.equals("DS")
												? invoiceRepository.getDsIssuedInvoicesDuringContract(
														resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
														calendar.getTime())
												: invoiceRepository.getNeIssuedInvoicesDuringContract(
														resume.getBranch().getBranchId(), dinamycCalendar.getTime(),
														calendar.getTime());
								
								Long discount = 0L;
								if (module.equals("NE")) {
									if (year - contract.getContractDate().getYear() == 1
											&& contract.getFirstYearFree() && contract.getDiscountSecondYear() > 0L) {
										discount = contract.getDiscountSecondYear();
									}
								}

								invoiceDetailMap = tempPlan.getAnnualBillDetail(issuedDocs, issuedDocsLastMonth,
										chargeSubscription, refill, discount);
							} else {
								invoiceDetailMap = tempPlan.getBillDetail(resume.getIssuedInvoices());
							}
						}

						if (!module.equals("DS")) {
							addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract,
									invoiceDetailMap, invoiceDetailFullMap, update, module);
						}

						if (update) {
							try {
								contractRepository.save(contract);
							} catch (Exception e) {
								System.out
										.println("Hubo un error al actualizar el contrato " + contract.getContractId());
							}
						}
					} else {
						System.out.println(
								"La sucursal " + resume.getBranch().getBranchId() + " tiene un contrato compartido.");
					}
				} else {
					System.out.println(
							"La sucursal " + resume.getBranch().getBranchId() + " tiene el contrato cancelado.");
				}
			} else {
				System.out.println("La sucursal " + resume.getBranch().getBranchId() + " no tiene contrato.");
			}
		}
	}

	/**
	 * Evalua todas las empresas que no emitieron documentos en el mes para determinar el cobro que se les debe realizar.
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param invoiceDetailFullMap (Variable en la que se guarda el detalle de todas las facturas que se van a generar).
	 * @param update (true si los cambios se deben guardar en la base de datos de la aplicación. En su defecto, false).
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 */
	public void getBillsToCollectByOtherCharges(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap, Boolean update, String module) {
		System.out.println("Running getBillsToCollectByOtherCharges - " + module);

		List<Contract> contracts = contractRepository.getContracts(module);

		Set<Long> keys = invoiceDetailFullMap.keySet();

		List<String> sharedContractsFilter = contractRepository.getSharedContracts(module);

		List<Contract> contractsFiltered = contracts.stream()
				.filter(c -> !keys.contains(c.getBranch().getBranchId())
						&& !sharedContractsFilter.contains(c.getContractId())
						&& (c.getModule().equals("FE") ? !c.getSharedMaintenance() : c.getModule().equals("NE"))
						&& cancellationRepository.findByContractId(c.getContractId()) == null)
				.collect(Collectors.toList());// TODO Check

		contractsFiltered.forEach(contract -> {
			System.out.println(contract.getContractId());
			LinkedHashMap<String, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			if (!contract.getSharedContract()) {
				Long[] valQuant = new Long[2];
				Date contractDate = contract.getReferencePaymentDate();
				PaymentType tempPlan = contract.getPaymentPlan();
				if (tempPlan.getDiscriminatorType() == 1 || tempPlan.getDiscriminatorType() == 5
						|| tempPlan.getDiscriminatorType() == 6) {
					if (tempPlan.getPaymentFrequency().getFrequencyId() == 4) {
						valQuant[0] = 1L;
						valQuant[1] = tempPlan.getPackagePrice();
						invoiceDetailMap.put(module.equals("FE") ? "192"
								: module.equals("NE") ? "182"
								: "185-1", valQuant);
					} else if (tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
						if (contractDate.getMonth() == month - 1 && (contractDate.getYear() + 1900) < year) {
							valQuant[0] = 1L;
							valQuant[1] = tempPlan.getPackagePrice();
							invoiceDetailMap.put(module.equals("FE") ? "194"
									: module.equals("NE") ? "184"
									: "185-2", valQuant);

							// --Fix date
							Calendar calendar = Calendar.getInstance();
							calendar.set(year.intValue(), month.intValue() - 1, contractDate.getDate());
							contract.setReferencePaymentDate(calendar.getTime());
							//System.out.println("Anualidad " + contract.getContractId() + " - " + calendar.getTime());

							if (update) {
								try {
									contractRepository.save(contract);
								} catch (Exception e) {
									System.out.println(
											"Hubo un error al actualizar el contrato " + contract.getContractId());
								}
							}
						}
					}
				} else if (tempPlan.getDiscriminatorType() == 2 && tempPlan.getMixedContract()) {
					valQuant[0] = 1L;
					valQuant[1] = tempPlan.getPackagePrice();
					invoiceDetailMap.put("192", valQuant);
				}
			}

			if (!module.equals("DS")) {
				addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract, invoiceDetailMap,
						invoiceDetailFullMap, update, module);
			}
		});
	}

	/**
	 * Evalua todas las empresas que emitieron documentos en el mes para determinar el cobro que se les debe realizar.
	 * Dichas empresas corresponden a contratos compartidos.
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param invoiceDetailFullMap (Variable en la que se guarda el detalle de todas las facturas que se van a generar).
	 * @param resumes (Listado de conteos de facturasa correspondientes al periodo).
	 * @param update (true si los cambios se deben guardar en la base de datos de la aplicación. En su defecto, false).
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 */
	public void getBillsToCollectDueSharedContracts(Long year, Long month,
			LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap, List<InvoiceResume> resumes,
			Boolean update, String module) {
		System.out.println("Running getBillsToCollectDueSharedContracts - " + module);
		List<Contract> contracts = contractRepository.getContracts(module);
		List<String> sharedContracts = contractRepository.getSharedContracts(module);
		List<Contract> filteredContracts = contracts.stream()
				.filter(contract -> sharedContracts.contains(contract.getContractId())
						&& cancellationRepository.findByContractId(contract.getContractId()) == null)
				.collect(Collectors.toList());

		List<Contract> contractConsumers = contracts.stream()
				.filter(contract -> contract.getSharedContract()
						&& cancellationRepository.findByContractId(contract.getContractId()) == null)
				.collect(Collectors.toList());

		for (Contract contract : filteredContracts) {

			System.out.println(contract.getContractId());
			LinkedHashMap<String, Long[]> invoiceDetailMap = new LinkedHashMap<>();
			PaymentType tempPlan = contract.getPaymentPlan();
			Date contractDate = contract.getReferencePaymentDate();

			List<Contract> tempContracts = contractConsumers.stream()
					.filter(c -> c.getSharedContractId().equalsIgnoreCase(contract.getContractId()))
					.collect(Collectors.toList());
			if (tempContracts != null) {
				List<Long> tempBranches = new ArrayList<>();
				tempBranches.add(contract.getBranch().getBranchId());
				tempContracts.forEach(c -> {
					tempBranches.add(c.getBranch().getBranchId());
				});
				Long issuedInvoices = 0L;
				if ((tempPlan.getDiscriminatorType() == 1 || tempPlan.getDiscriminatorType() == 5
						|| tempPlan.getDiscriminatorType() == 6)
						&& tempPlan.getPaymentFrequency().getFrequencyId() == 8) {
					// Sumar las emitidas de ese año para cada una de las empresas que comparten el
					// contrato
					boolean chargeSubscription = false;
					Calendar calendar = Calendar.getInstance();
					Calendar dinamycCalendar = Calendar.getInstance();
					calendar.set(year.intValue(), month.intValue() - 1, 1, 0, 0, 0);
					dinamycCalendar.set(contractDate.getYear() + 1900, contractDate.getMonth(), contractDate.getDate(),
							23, 59, 59);
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
						contract.setReferencePaymentDate(calendar.getTime());
					} else {
						calendar.set(year.intValue(), month.intValue(), 1);
					}

					for (Long branchId : tempBranches) {
						// issuedInvoices += contractFactory.getIssuedInvoicesDuringContract(module,
						// branchId,
						// dinamycCalendar.getTime(), calendar.getTime());
						issuedInvoices += module.equals("FE")
								? invoiceRepository.getFeIssuedInvoicesDuringContract(branchId,
										dinamycCalendar.getTime(), calendar.getTime())
								: module.equals("DS")
										? invoiceRepository.getDsIssuedInvoicesDuringContract(branchId,
												dinamycCalendar.getTime(), calendar.getTime())
										: invoiceRepository.getNeIssuedInvoicesDuringContract(branchId,
												dinamycCalendar.getTime(), calendar.getTime());

					}
					// Calcular las facturas emitidas del mes anterior

					Boolean refill = false;
					if (issuedInvoices > tempPlan.getDocumentQuantity()) {
						// Validate if it is a Centsys' company to refill the contract
						Alliance alliance = contract.getBranch().getClient().getAlliance();
						if (alliance != null) {
							Long allianceId = alliance.getAllianceId();
							if (module.equals("FE") ? allianceId == 4L : (module.equals("NE") || module.equals("DS"))) {
								refill = true;
								chargeSubscription = true;

								Calendar endReferenceCalendar = Calendar.getInstance();
								endReferenceCalendar.setTime(calendar.getTime());
								endReferenceCalendar.add(Calendar.DATE, 1);
								Calendar startReferenceCalendar = Calendar.getInstance();
								startReferenceCalendar.setTime(calendar.getTime());
								startReferenceCalendar.set(Calendar.MONTH, (int) (month - 1));
								startReferenceCalendar.set(Calendar.DATE, 1);

								for (Date date = startReferenceCalendar.getTime(); date
										.before(endReferenceCalendar.getTime()); startReferenceCalendar
												.add(Calendar.DATE, 1), date = startReferenceCalendar.getTime()) {
									//System.out.println(date);
									Long tempDocsCount = 0L;
									for (Long branchId : tempBranches) {
										// tempDocsCount += contractFactory.getIssuedInvoicesDuringContract(module,
										// branchId, dinamycCalendar.getTime(), date);
										tempDocsCount += module.equals("FE")
												? invoiceRepository.getFeIssuedInvoicesDuringContract(branchId,
														dinamycCalendar.getTime(), date)
												: module.equals("DS")
														? invoiceRepository.getDsIssuedInvoicesDuringContract(branchId,
																dinamycCalendar.getTime(), date)
														: invoiceRepository.getNeIssuedInvoicesDuringContract(branchId,
																dinamycCalendar.getTime(), date);
									}

									if (tempDocsCount > tempPlan.getDocumentQuantity()) {
										if (startReferenceCalendar.get(Calendar.MONTH) > (int) (month - 1L)) {
											startReferenceCalendar.add(Calendar.DATE, -1);
										}
										/*System.out.println("Refill: " + contract.getContractId() + " - " + tempDocsCount
												+ " - " + startReferenceCalendar.getTime());*/
										contract.setReferencePaymentDate(startReferenceCalendar.getTime());
										endReferenceCalendar.setTime(date);
									}
								}
							}
							
							if (module.equals("DS")) {
								String newPackageName = tempPlan.getJsonPackageInfo(issuedInvoices.intValue()).get(0);
								Integer newPackageDocQuantity = Integer.parseInt(tempPlan.getJsonPackageInfo(issuedInvoices.intValue()).get(2));
								Long newPackagePrice = Long.parseLong(tempPlan.getJsonPackageInfo(issuedInvoices.intValue()).get(3));
								
								PaymentType newPlan = paymentTypeRepository
										.findPackageByParams(6, tempPlan.getCostRange(),
												newPackageName,	newPackageDocQuantity, newPackagePrice,
												0L, tempPlan.getPaymentFrequency(), module, false, true);
								
								if (newPlan == null) {
									newPlan = new PaymentType();
									newPlan.setDiscriminatorType(6);
									newPlan.setCostRange(tempPlan.getCostRange());
									newPlan.setPackageName(newPackageName);
									newPlan.setDocumentQuantity(newPackageDocQuantity);
									newPlan.setPackagePrice(newPackagePrice);
									newPlan.setDocumentPrice(0L);
									newPlan.setPaymentFrequency(tempPlan.getPaymentFrequency());
									newPlan.setModulePlan("DS");
									newPlan.setMixedContract(false);
									newPlan.setSelfAdjusting(true);
									
									newPlan = paymentTypeRepository.save(newPlan);
								}
								
								contract.setPaymentPlan(newPlan);
							}
						}
					}
					calendar.set(year.intValue(), month.intValue() - 1, 1);

					Long issuedDocsLastMonth = 0L;
					for (Long branchId : tempBranches) {
						// issuedDocsLastMonth +=
						// contractFactory.getIssuedInvoicesDuringContract(module, branchId,
						// dinamycCalendar.getTime(), calendar.getTime());

						issuedDocsLastMonth += module.equals("FE")
								? invoiceRepository.getFeIssuedInvoicesDuringContract(branchId,
										dinamycCalendar.getTime(), calendar.getTime())
								: module.equals("DS")
										? invoiceRepository.getDsIssuedInvoicesDuringContract(branchId,
												dinamycCalendar.getTime(), calendar.getTime())
										: invoiceRepository.getNeIssuedInvoicesDuringContract(branchId,
												dinamycCalendar.getTime(), calendar.getTime());
					}
					
					Long discount = 0L;
					if (module.equals("NE")) {
						if (year - contract.getContractDate().getYear() == 1
								&& contract.getFirstYearFree() && contract.getDiscountSecondYear() > 0L) {
							discount = contract.getDiscountSecondYear();
						}
					}

					invoiceDetailMap = tempPlan.getAnnualBillDetail(issuedInvoices, issuedDocsLastMonth,
							chargeSubscription, refill, discount);
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
					contractRepository.save(contract);
				} catch (Exception e) {
					System.out.println("Hubo un error al actualizar el contrato " + contract.getContractId());
				}
			}

			if (!module.equals("DS")) {
				addMoreDetailsToPay(year, month, contract.getBranch().getBranchId(), contract, invoiceDetailMap,
						invoiceDetailFullMap, update, module);
			}
		}

	}

	/**
	 * Evalua si una sucursal tiene pendiente el cobro de mantenimiento y/o implementación.
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param branchId (ID de la sucursal).
	 * @param contract (Contrato que se va a evaluar).
	 * @param invoiceDetailMap (Variable en la que se almacena el detalle de la factura "concepto, cantidad y valor").
	 * @param invoiceDetailFullMap (Variable en la que se guarda el detalle de todas las facturas que se van a generar).
	 * @param (true si los cambios se deben guardar en la base de datos de la aplicación. En su defecto, false).
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 */
	public void addMoreDetailsToPay(Long year, Long month, Long branchId, Contract contract,
			LinkedHashMap<String, Long[]> invoiceDetailMap,
			LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap, Boolean update, String module) {
		Alliance alliance = contract.getBranch().getClient().getAlliance();
		Date contractDate = contract.getContractDate();

		if (cancellationRepository.findByContractId(contract.getContractId()) == null) {
			if (alliance != null) {
				if (alliance.getAllianceId() == 4L) {
					contractDate = Optional.ofNullable(contract.getFirstIssueDate()).orElse(contractDate);
				}
			}

			// Cobro de implementacion pendiente
			if (contract.getImplementationAlreadyPaid() == false) {
				Long[] valueXQuantity = { 1L, contract.getImplementationCost() };
				invoiceDetailMap.put(module.equals("FE") ? "190" 
						: module.equals("NE") ? "183"
						: "185-3", valueXQuantity);
				contract.setImplementationAlreadyPaid(true);
				if (update) {
					try {
						contractRepository.save(contract);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// Cobro de mantenimiento vencido de ese periodo
			if (contract.getModule().equals("FE")) {
				if (!contract.getSharedMaintenance()) {
					if (contractDate.getYear() + 1900 < year) {
						if (contractDate.getMonth() + 1 == month) {
							Long[] valueXQuantity = { 1L,
									contract.getMaintenanceType().getMaintenanceCost() };
							invoiceDetailMap.put("196", valueXQuantity);
						}
					}

					// Cobro de mantenimiento vencido de periodos pasados
					if (contract.getMaintenanceAlreadyPaid() == false) {
						Long[] valueXQuantity = { 1L, contract.getMaintenanceType().getMaintenanceCost() };
						invoiceDetailMap.put("200", valueXQuantity);
						contract.setMaintenanceAlreadyPaid(true);
						if (update) {
							try {
								contractRepository.save(contract);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		if (!invoiceDetailMap.isEmpty()) {
			invoiceDetailFullMap.put(branchId, invoiceDetailMap);
		}
	}

	/**
	 * Guarda las facturas que se generaron en el mes, bien sea en la base de datos o en una variable por si se está realizando
	 * una simulación.
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param invoiceDetailFullMap (Variable en la que se encuentra todo el detalle de los conceptos que se van a cobrar en las facturas).
	 * @param save (true si se debe guardar en la base de datos, o en su defecto, false).
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 */
	public void saveBills(Long year, Long month, LinkedHashMap<Long, LinkedHashMap<String, Long[]>> invoiceDetailFullMap,
			Boolean save, String module) {
		System.out.println("Running saveBills");
		this.tempBillProducts = new ArrayList<>();
		Calendar calendar = null;
		Bill bill = null;
		Contract tempContract = null;
		BillProduct billProduct = null;
		for (Map.Entry<Long, LinkedHashMap<String, Long[]>> invoiceDetailMap : invoiceDetailFullMap.entrySet()) {
			calendar = Calendar.getInstance();
			cleanDetailMap(invoiceDetailMap.getValue());
			if (!invoiceDetailMap.getValue().isEmpty()) {
				bill = new Bill();
				tempContract = contractRepository.findByBranchId(invoiceDetailMap.getKey(), module);
				
				if (!tempContract.getContractId().equals("OTROSI-006-18") 
						&& !tempContract.getContractId().equals("INTELSOFT-0")) {
					bill.setBillNumber(this.lastInvoiceNumber);
					bill.setCreationDate(calendar.getTime());
					calendar.add(Calendar.DATE, 30);
					bill.setExpirationDate(calendar.getTime());
					bill.setYear(year);
					bill.setMonth(month);
					bill.setBranch(branchRepository.findByBranchId(invoiceDetailMap.getKey()));
					bill.setDescription(generateInvoiceDescription(year, month, tempContract, invoiceDetailMap.getValue(), module));
					Double ponderedIpc = this.calculateIpcIncrease(tempContract, year);
					Double ipcUntilLastYear = this.calculateIpcIncrease(tempContract, year - 1);
					if (save) {
						try {
							billRepository.save(bill);
							System.out.println("Saved " + bill.getBillId() + ", " + bill.getDescription());
							InvoiceResume row = invoiceResumeRepository.findByBranchYearMonth(invoiceDetailMap.getKey(),
									year, month, module);
							if (row != null) {
								row.setBill(bill);
								invoiceResumeRepository.save(row);
							}
							this.lastInvoiceNumber++;
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						for (Map.Entry<String, Long[]> concept : invoiceDetailMap.getValue().entrySet()) {
							billProduct = new BillProduct();
							billProduct.setBill(bill);
							billProduct.setProduct(productRepository.findByProductId(concept.getKey()));
							billProduct.setQuantity(concept.getValue()[0].longValue());
							billProduct.setPrice(concept.getKey().equals("190") ? concept.getValue()[1]
									: concept.getKey().equals("200")
											? new Double(concept.getValue()[1] * ipcUntilLastYear).longValue()
											: new Double(concept.getValue()[1] * ponderedIpc).longValue());
							try {
								billProductRepository.save(billProduct);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						for (Map.Entry<String, Long[]> concept : invoiceDetailMap.getValue().entrySet()) {
							billProduct = new BillProduct();
							billProduct.setBill(bill);
							billProduct.setProduct(productRepository.findByProductId(concept.getKey()));
							billProduct.setQuantity(concept.getValue()[0].longValue());
							billProduct.setPrice(concept.getKey().equals("190") ? concept.getValue()[1]
									: concept.getKey().equals("200")
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
	}

	/**
	 * Elimina todos los conceptos de cobro que se encuentren en cero.
	 * @param invoiceDetailMap (Detalle de la factura que se está evaluando).
	 * @return LinkedHashMap sin los conceptos que se encuentran en cero.
	 */
	public LinkedHashMap<String, Long[]> cleanDetailMap(LinkedHashMap<String, Long[]> invoiceDetailMap) {
		List<String> keysToRemove = new ArrayList<>();
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

	/**
	 * Genera la descripción que tendrá la factura
	 * @param year (Año en que se genera la facturación).
	 * @param month (Mes en que se genera la facturación).
	 * @param contract (Contrato que se está evaluando)
	 * @param invoiceDetailMap (Variable en la que se almacena el detalle de la factura "concepto, cantidad y valor").
	 * @param module (Acrónimo que indica a cual servicio corresponde la ejecución de facturación en curso, bien sea FE, NE o DS).
	 * @return La descripción que tendra la factura generada.
	 */
	public String generateInvoiceDescription(Long year, Long month, Contract contract,
			LinkedHashMap<String, Long[]> invoiceDetailMap, String module) {
		cleanDetailMap(invoiceDetailMap);
		String description = "";
		String branchText = "Sucursal \"" + contract.getBranch().getCode() + " - " + contract.getBranch().getName() + "\". ";
		Set<String> keys = invoiceDetailMap.keySet();
		String monthString = getMonthFromNumber(month);

		switch (module) {
			case "NE": 
				if (keys.contains("184")) {
					description = "Cobro anualidad nomina electrónica " + monthString + " " + year + " - " + (year + 1) + ". ";
				} else {
					description = "Cobro mensual nómina electrónica correspondiente al mes de " + monthString + " " + year + ". ";
				}
				break;
			case "DS": 
				if (keys.contains("185-2")) {
					description = "Cobro anualidad documento soporte " + monthString + " " + year + " - " + (year + 1) + ". ";
				} else {
					description = "Cobro mensual documento soporte correspondiente al mes de " + monthString + " " + year + ". ";
				}
				break;
			case "FE": 
			default:
				if (keys.contains("194")) {
					description = "Facturación anual " + monthString + " " + year + " - " + (year + 1) + ". ";
				} else {
					description = "Facturación correspondiente al mes de " + monthString + " " + year + ". ";
				}

				if (keys.contains("196")) {
					description += "Mantenimiento Facturación Electrónica entre " + monthString + " " + year + "-" + (year + 1) + ". ";
				}

				if (keys.contains("200")) {
					description += "Mantenimiento Facturación Electrónica periodos anteriores vencidos." + ". ";
				}
		}

		return description + branchText;
	}

	/**
	 * Obtiene el nombre del mes según el número indicado.
	 * @param month (Mes a evaluar entre 1 a 12)
	 * @return El nombre del mes.
	 */
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
	public Object[] generateFaceldiFile(Long year, Long month, String simulation, String module) {
		System.out.println("Running generateFaceldiFile");
		List<String> rowLines = new ArrayList<>();
		if (!simulation.equalsIgnoreCase("S")) {
			List<Bill> bills = billRepository.getFaceldiReport(year, month);
			bills.forEach(bill -> {
				Branch branch = bill.getBranch();
				Contract contract = contractRepository.findByBranchId(bill.getBranch().getBranchId(), module);
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
					row.setPrCodigoProducto(module.equals("FE") && billProduct.getProduct().getProductId().equals("200") ? "196"
							: billProduct.getProduct().getProductId());
					row.setPrDescripcion(
							module.equals("FE") && billProduct.getProduct().getProductId().equals("200") ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
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
				Contract contract = contractRepository.findByBranchId(billProduct.getBill().getBranch().getBranchId(),
						module);
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
				row.setPrCodigoProducto(module.equals("FE") && billProduct.getProduct().getProductId().equals("200") ? "196"
						: billProduct.getProduct().getProductId());
				row.setPrDescripcion(
						module.equals("FE") && billProduct.getProduct().getProductId().equals("200") ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
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
	public Object[] generateSmartFile(Long year, Long month, String simulation, String module) {
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
					row.setCActivityId(module.equals("FE") ? (alliance != null ? alliance.getSmartCC() : 15L)
							: module.equals("NE") ? 100L : 101L); 
					row.setCInvoiceLineCInvoiceId(billProduct.getBill().getBillNumber());
					row.setCInvoiceLine(itemIndex);
					row.setCInvoiceLineMProductId(billProduct.getProduct().getProductId().equals("200") ? "196"
							: billProduct.getProduct().getProductId());
					row.setCInvoiceLineDescription(row.getDescription() + " "
							+ (billProduct.getProduct().getProductId().equals("200")
									? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
									: billProduct.getProduct().getDescription()));
					row.setCInvoiceLineQtyEntered(billProduct.getQuantity());
					row.setCInvoiceLinePriceEntered(billProduct.getPrice());
					row.setCInvoiceLinePriceList(billProduct.getPrice());
					row.setIdType(billProduct.getBill().getBranch().getClient().getTipoIdentificacion());
					if (!(row.getCInvoiceLinePriceEntered() == 0 || row.getCInvoiceLineQtyEntered() == 0)) {
						rowLines.add(row.toString());
					}
				};
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
				row.setCActivityId(module.equals("FE") ? (alliance != null ? alliance.getSmartCC() : 15L)
						: module.equals("NE") ? 100L : 101L); 
				row.setCInvoiceLineCInvoiceId(billProduct.getBill().getBillNumber());
				row.setCInvoiceLine(itemIndex);
				row.setCInvoiceLineMProductId(billProduct.getProduct().getProductId().equals("200") ? "196"
						: billProduct.getProduct().getProductId());
				row.setCInvoiceLineDescription(row.getDescription() + " "
						+ (billProduct.getProduct().getProductId().equals("200") ? "SOPORTE Y MANTENIMIENTO ANUAL  FACELDI"
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