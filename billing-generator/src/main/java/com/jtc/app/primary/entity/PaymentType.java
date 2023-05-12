package com.jtc.app.primary.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payment_plan")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PaymentType {

	protected final static String[][] CONCEPTS = {{"190","IMPLEMENTACION FACTURACION ELECTRONICA"},
												{"191","DOCUMENTOS EMITIDOS FACELDI"},
												{"192","MENSUALIDAD FIJA / PLAN FIJO DOC EMITIDOS FACELDI"},
												{"194","ANUALIDAD FIJA FACELDI"},
												{"196","SOPORTE Y MANTENIMIENTO ANUAL FACELDI"}, // OK
												{"181","DOCUMENTOS EMITIDOS NE"},
												{"182","MENSUALIDAD NOMINA ELECTRONICA"},
												{"183","IMPLEMENTACION NOMINA ELECTRONICA"},
												{"184","ANUALIDAD NOMINA ELECTRONICA"},
												{"185","DOCUMENTOS EMITIDOS DS"},
												{"185-1","MENSUALIDAD DS"},
												{"185-2","ANUALIDAD DS"},
												{"185-3","IMPLEMENTACION DS"}};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_plan_id")
	private Long paymentPlanId;
	@Column(name = "discriminator_type")
	private Integer discriminatorType;
	@Column(name = "module_plan")
	private String modulePlan;
	@Column(name = "package_name")
	private String packageName;
	@Column(name = "document_quantity")
	private Integer documentQuantity;
	@Column(name = "package_price")
	private Long packagePrice;
	@Column(name = "document_price")
	private Long documentPrice;
	@ManyToOne
	@JoinColumn(name = "payment_frequency_id", nullable = false)
	private Frequency paymentFrequency;
	@Column(name = "cost_range", length = 500)
	private String costRange;
	@Column(name = "plan_description", length = 500)
	private String planDescription;
	@Column(name = "self_adjusting")
	private Boolean selfAdjusting;
	@Column(name = "mixed_contract")
	private Boolean mixedContract;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paymentPlan")
	private Set<Contract> contracts;
	
	/**
	 * Calcula los conceptos que se deben cobrar a ese cliente si es de tipo facturación mensual 
	 * @param docQuantity (Cantidad de documentos emitidos en el mes)
	 * @return LinkedHashMap que contiene el concepto, las cantidades y sus respectivos valores
	 */
	public LinkedHashMap<String, Long[]> getBillDetail(Long docQuantity) {
		Long[] valQuant = new Long[2];
		LinkedHashMap<String, Long[]> invoiceDetailMap = new LinkedHashMap<>();
		switch (this.discriminatorType) {
		case 1: 
		case 5:
			int limit = 0; 
			limit = this.documentQuantity;
			
			// -1 Si corresponde a un plan autoajustable
			if (limit == -1) {
				valQuant[0] = 1L;
				valQuant[1] = getPricePerJsonPackage(this.packageName);
			} else {
				valQuant[0] = 1L;
				valQuant[1] = this.packagePrice;				
			}
			invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[2][0] 
					: modulePlan.equals("NE") ? CONCEPTS[6][0]
					: CONCEPTS[10][0], valQuant);
			if (docQuantity > limit && limit != -1) {
				valQuant = new Long[2];
				valQuant[0] = docQuantity - limit;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[1][0] 
						: modulePlan.equals("NE") ? CONCEPTS[5][0]
						: CONCEPTS[9][0], valQuant);
			}
			return invoiceDetailMap;
		case 2:
		case 4:
			valQuant[0] = docQuantity;
			valQuant[1] = this.documentPrice;
			invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[1][0] 
					: modulePlan.equals("NE") ? CONCEPTS[5][0]
					: CONCEPTS[9][0], valQuant);
			if (this.mixedContract) {
				valQuant = new Long[2];
				valQuant[0] = 1L;
				valQuant[1] = this.packagePrice;
				invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[2][0] 
						: modulePlan.equals("NE") ? CONCEPTS[6][0]
						: CONCEPTS[10][0], valQuant);
			}
			return invoiceDetailMap;
		case 3: 
			Long pricePerDocument = getPricePerDocument(docQuantity.intValue());
			valQuant[0] = docQuantity;
			valQuant[1] = pricePerDocument;
			invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[1][0] 
					: modulePlan.equals("NE") ? CONCEPTS[5][0]
					: CONCEPTS[9][0], valQuant);
			return invoiceDetailMap;
		default:
			return null;
		}
	}
	
	/**
	 * Calcula los conceptos que se deben cobrar a ese cliente si es de tipo facturación anual
	 * @param issuedDocQuantity (Cantidad de documentos emitidos desde su último cobro de anualidad hasta la fecha de facturación)
	 * @param issuedDocsLastMonth (Cantidad de documentos emitidos en el mes anterior)
	 * @param annualCharge (Indica si se debe realizar cobro de la anualidad)
	 * @param reFillPlan (Indica si se debe realizar cobro de la anualidad por haber consumido su paquete de documentos o si se 
	 * deben cobrar documentos emitidos adicionales)
	 * @param discount (Porcentaje de descuento que aplica en su primer año de cobro)
	 * @return LinkedHashMap que contiene el concepto, las cantidades y sus respectivos valores
	 */
	public LinkedHashMap<String, Long[]> getAnnualBillDetail(Long issuedDocQuantity, Long issuedDocsLastMonth, 
			boolean annualCharge, boolean reFillPlan, Long discount) {
		int limit = 0;
		LinkedHashMap<String, Long[]> invoiceDetailMap = new LinkedHashMap<>();
		limit = this.documentQuantity;
		
		if (!reFillPlan) {
			if (issuedDocsLastMonth > limit) {
				Long[] valQuant = new Long[2];
				valQuant[0] = issuedDocQuantity - issuedDocsLastMonth;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[1][0] 
						: modulePlan.equals("NE") ? CONCEPTS[5][0]
						: CONCEPTS[9][0], valQuant);
			} else if (issuedDocQuantity > limit){
				Long[] valQuant = new Long[2];
				valQuant[0] = issuedDocQuantity - limit;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[1][0] 
						: modulePlan.equals("NE") ? CONCEPTS[5][0]
						: CONCEPTS[9][0], valQuant);
			}			
		}
		
		if (annualCharge) {
			Long[] valQuant = new Long[2];
			valQuant[0] = 1L;
			valQuant[1] = discount != null && discount > 0L ? this.packagePrice * (discount / 100) : this.packagePrice;
			invoiceDetailMap.put(modulePlan.equals("FE") ? CONCEPTS[3][0]
							: modulePlan.equals("NE") ? CONCEPTS[8][0]
							: CONCEPTS[11][0], valQuant);
		}
		
		return invoiceDetailMap;
	}
	
	/**
	 * Obtiene la cantidad de documentos que contiene un plan según su nombre
	 * @param packageName (Nombre del paquete)
	 * @return Integer que indica la cantidad de documentos del paquete
	 */
	public Integer getDocumentQuantityByPackageName(String packageName) {
		switch (packageName) {
			case "Fantasía": return 5;
			case "Bronce": return 10;
			case "Plata": return 20;
			case "Oro": return 50;
			case "Bolsa 1": return 250;
			case "Bolsa 2": return 500;
			case "Bolsa 3": return 1000;
			case "Independientes": return 25;
			case "Emprendedores": return 100;
			case "Pymes": return 300;
			case "Crecimiento": return 500;
			case "Empresarial": return 1000;
			case "Plan S": 
			case "Plan M": 
			case "Plan L": 
			case "Plan XL": return -1;
			default: 
		}
		return null;
	}
	
	/**
	 * Extrae el valor por cada documento emitido contenido en un JSON de tarifas con la siguiente estructura:
	 * {"Rangos":[{"Desde":0,"Hasta":1000,"Valor":100},{....}]}
	 * @param docQuantity (Cantidad de documentos emitidos)
	 * @return Long con el valor que se debe cobrar por cada documento emitido
	 */
	public Long getPricePerDocument(int docQuantity) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		JSONObject j = new JSONObject(this.costRange);
		j.getJSONArray("Rangos").forEach(x -> {
			JSONObject object = new JSONObject(x.toString());
			Long min = Long.parseLong(object.get("Desde").toString());
			Long max = Long.parseLong(object.get("Hasta").toString());
			if (docQuantity >= min && docQuantity <= max) {
				Long pricePerDocument = Long.parseLong(object.get("Valor").toString());
				map.put("Desde", min);
				map.put("Hasta", min);
				map.put("Valor", pricePerDocument);
			}
		});
		return map.get("Valor");
	}
	
	/**
	 * Extrae el valor de un paquete de documentos contenidos en un JSON de tarifas con la siguiente estructura:
	 * {"Planes":[{"Nombre":"Plan S","Desde":0,"Hasta":100,"Valor":29900},{....}]}
	 * @param docQuantity (Cantidad de documentos emitidos)
	 * @return Long con el valor que se debe cobrar por el paquete de acuerdo con los documentos emitidos
	 */
	public Long getPricePerJsonPackage(String pckgName) {
		LinkedHashMap<String, Long> map = new LinkedHashMap<>();
		JSONObject j = new JSONObject(this.costRange);
		j.getJSONArray("Planes").forEach(x -> {
			JSONObject object = new JSONObject(x.toString());
				String packageName = object.get("Nombre").toString();
				Long pricePerPackage = Long.parseLong(object.get("Valor").toString());
				map.put(packageName, pricePerPackage);
		});
		return map.get(pckgName);
	}
	
	/**
	 * Extrae los detalles de un paquete de documentos contenidos en un JSON de tarifas con la siguiente estructura:
	 * {"Planes":[{"Nombre":"Plan S","Desde":0,"Hasta":100,"Valor":29900},{....}]}
	 * @param docQuantity (Cantidad de documentos emitidos)
	 * @return List: Con la siguiente información según la posición (0) Nombre, (1) Desde, (2) Hasta, (3) Valor
	 */
	public List<String> getJsonPackageInfo(int docQuantity) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		ArrayList<String> responseList = new ArrayList<>();
		JSONObject j = new JSONObject(this.costRange);
		j.getJSONArray("Planes").forEach(x -> {
			JSONObject object = new JSONObject(x.toString());
			String name = object.get("Nombre").toString();
			Long min = Long.parseLong(object.get("Desde").toString());
			Long max = Long.parseLong(object.get("Hasta").toString());
			if (docQuantity >= min && docQuantity <= max) {
				map.put("Nombre", name);
				responseList.add(name);
				responseList.add(object.get("Desde").toString());
				responseList.add(object.get("Hasta").toString());
				responseList.add(object.get("Valor").toString());
			}
		});
		return responseList;
	}
	
	/**
	 * Genera la descripción del plan de facturación electrónica con los atributos del objeto padre que invoque el método.
	 * Dicha descripción se almacena en el atributo "planDescription" del objeto.
	 */
	public void generateFePlanDescription() {
		switch (this.discriminatorType) {
			case 1:
				if (this.packageName == null || this.packageName == "") {
					if (this.paymentFrequency.getFrequencyId() == 4) {
						this.planDescription = "Mensualidad fija x " + this.documentQuantity + " documentos";
					} else {
						this.planDescription = "Anualidad fija x " + this.documentQuantity + " documentos";
					}
				} else {
					this.planDescription = "Plan " + this.packageName + " de 0 a " + 
							getDocumentQuantityByPackageName(this.packageName) + " documentos";
				}
				
				if (this.documentPrice > 0L) {
					this.planDescription += " + $" + this.documentPrice + " por documento extra";
				}
				break;
			case 2:
				this.planDescription = "Cobro por documento emitido ($" + this.documentPrice + ")."; 
				if (this.mixedContract) {
					this.planDescription += "+ Mensualidad fija";
				}
				break;
			case 3:
				this.planDescription = "Cobro por rango de documentos emitidos en el mes"; break;
			default:
				this.planDescription = null;
		}
	}
	
	/**
	 * Genera la descripción del plan de nómina electrónica con los atributos del objeto padre que invoque el método.
	 * Dicha descripción se almacena en el atributo "planDescription" del objeto.
	 */
	public void generateNePlanDescription(String description) {
		if (this.paymentFrequency.getFrequencyId() == 4) {
			this.planDescription = "Mensualidad fija " + description + " (" +  this.documentQuantity + ").";
		} else {
			this.planDescription = "Anualidad fija " + description + " (" +  this.documentQuantity + ").";
		}
	}
	
	/**
	 * Genera la descripción del plan de nómina electrónica con los atributos del objeto padre que invoque el método y 
	 * ajustando los valores con el IPC que les aplique.
	 * Dicha descripción se almacena en el atributo "planDescription" del objeto.
	 */
	public String getGenerateAdjustedPlanDescription(Double increaseRate) {
		switch (this.discriminatorType) {
			case 1:
				if (this.packageName == null || this.packageName == "") {
					if (this.paymentFrequency.getFrequencyId() == 4) {
						this.planDescription = "Mensualidad fija x " + this.documentQuantity + " documentos";
					} else {
						this.planDescription = "Anualidad fija x " + this.documentQuantity + " documentos";
					}
				} else {
					if (!(this.planDescription.contains("Plan S") || this.planDescription.contains("Plan M") || this.planDescription.contains("Plan L"))) {
						this.planDescription = "Plan " + this.packageName + " de 0 a " + 
								getDocumentQuantityByPackageName(this.packageName) + " documentos";
					}
				}
				
				if (this.documentPrice > 0L) {
					this.planDescription += " + $" + new Double(this.documentPrice * increaseRate).longValue() + " por documento extra";
				}
				break;
			case 2:
				this.planDescription = "Cobro por documento emitido ($" + new Double(this.documentPrice * increaseRate).longValue() + ")"; 
				if (this.mixedContract) {
					this.planDescription += "+ Mensualidad fija";
				}
				break;
			case 3:
				this.planDescription = "Cobro por rango de documentos emitidos en el mes"; break;
			default:
				this.planDescription = null;
		}
		return this.planDescription;
	}

}
