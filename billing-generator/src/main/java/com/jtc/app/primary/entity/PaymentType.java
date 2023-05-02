package com.jtc.app.primary.entity;

import java.util.LinkedHashMap;
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
												{"182","MENSUALIDAD NOMINA ELECTRONICA"},
												{"183","IMPLEMENTACION NOMINA ELECTRONICA"},
												{"184","ANUALIDAD NOMINA ELECTRONICA"}};
												//{"300","IMPLEMENTACION DOCUMENTO SOPORTE"},
												//{"301","DOCUMENTOS EMITIDOS FACELDI - DOCUMENTO SOPORTE"},
												//{"302","MENSUALIDAD FIJA / PLAN FIJO DOC EMITIDOS FACELDI - DOCUMENTO SOPORTE"},
												//{"304","ANUALIDAD FIJA FACELDI - DOCUMENTO SOPORTE"},
												//{"306","SOPORTE Y MANTENIMIENTO ANUAL FACELDI - DOCUMENTO SOPORTE"}};
	
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
	
	public LinkedHashMap<Long, Long[]> getBillDetail(Long docQuantity) {
		Long[] valQuant = new Long[2];
		LinkedHashMap<Long, Long[]> invoiceDetailMap = new LinkedHashMap<>();
		switch (this.discriminatorType) {
		case 1: 
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
			invoiceDetailMap.put(Long.parseLong(CONCEPTS[2][0]), valQuant);
			if (docQuantity > limit && limit != -1) {
				valQuant = new Long[2];
				valQuant[0] = docQuantity - limit;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(Long.parseLong(CONCEPTS[1][0]), valQuant);
			}
			return invoiceDetailMap;
		case 2:
			valQuant[0] = docQuantity;
			valQuant[1] = this.documentPrice;
			invoiceDetailMap.put(Long.parseLong(CONCEPTS[1][0]), valQuant);
			if (this.mixedContract) {
				valQuant = new Long[2];
				valQuant[0] = 1L;
				valQuant[1] = this.packagePrice;
				invoiceDetailMap.put(Long.parseLong(CONCEPTS[2][0]), valQuant);
			}
			return invoiceDetailMap;
		case 3: 
			Long pricePerDocument = getPricePerDocument(docQuantity.intValue());
			valQuant[0] = docQuantity;
			valQuant[1] = pricePerDocument;
			invoiceDetailMap.put(Long.parseLong(CONCEPTS[1][0]), valQuant);
			return invoiceDetailMap;
		default:
			return null;
		}
	}
	
	public LinkedHashMap<Long, Long[]> getAnnualBillDetail(Long issuedDocQuantity, Long issuedDocsLastMonth, 
			boolean annualCharge, boolean reFillPlan, Long discount) {
		int limit = 0;
		LinkedHashMap<Long, Long[]> invoiceDetailMap = new LinkedHashMap<>();
		limit = this.documentQuantity;
		
		if (!reFillPlan) {
			if (issuedDocsLastMonth > limit) {
				Long[] valQuant = new Long[2];
				valQuant[0] = issuedDocQuantity - issuedDocsLastMonth;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(Long.parseLong(CONCEPTS[1][0]), valQuant);
			} else if (issuedDocQuantity > limit){
				Long[] valQuant = new Long[2];
				valQuant[0] = issuedDocQuantity - limit;
				valQuant[1] = this.documentPrice;
				invoiceDetailMap.put(Long.parseLong(CONCEPTS[1][0]), valQuant);
			}			
		}
		
		if (annualCharge) {
			Long[] valQuant = new Long[2];
			valQuant[0] = 1L;
			valQuant[1] = discount != null && discount > 0L ? this.packagePrice * (discount / 100) : this.packagePrice;
			invoiceDetailMap.put(modulePlan.equals("FE") ? Long.parseLong(CONCEPTS[3][0])
							: Long.parseLong(CONCEPTS[7][0]), valQuant);
		}
		
		return invoiceDetailMap;
	}
	
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
	
	public String getJsonPackageName(int docQuantity) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		JSONObject j = new JSONObject(this.costRange);
		j.getJSONArray("Planes").forEach(x -> {
			JSONObject object = new JSONObject(x.toString());
			String name = object.get("Nombre").toString();
			Long min = Long.parseLong(object.get("Desde").toString());
			Long max = Long.parseLong(object.get("Hasta").toString());
			if (docQuantity >= min && docQuantity <= max) {
				map.put("Nombre", name);
			}
		});
		return map.get("Nombre");
	}
	
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
	
	public void generateNePlanDescription(String description) {
		if (this.paymentFrequency.getFrequencyId() == 4) {
			this.planDescription = "Mensualidad fija " + description + " (" +  this.documentQuantity + ").";
		} else {
			this.planDescription = "Anualidad fija " + description + " (" +  this.documentQuantity + ").";
		}
	}
	
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
