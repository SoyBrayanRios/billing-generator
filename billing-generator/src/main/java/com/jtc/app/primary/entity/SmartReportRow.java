package com.jtc.app.primary.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SmartReportRow {
	
	private String adOrgId = "JAIME TORRES C Y CIA SA";
	private Long documentNo;
	private String description;
	private String cDocTypeTargetId = "Factura CXC";
	private Date dateInvoiced;
	private Date dateAcct;
	private String cBPartnerId;
	private String adUserId = "";
	private String mPriceListId = "FAC ELECTRONICA";
	private String salesRepId = "jaimeAdmin";
	private String paymentRule = "";
	private String cPaymentTermId = "1000002";
	private Integer cProjectId = 7;
	private Long cActivityId;
	private String cInvoiceLineAdOrgId = "JAIME TORRES C Y CIA SA";
	private Long cInvoiceLineCInvoiceId;
	private Integer cInvoiceLine;
	private Long cInvoiceLineMProductId;
	private String cInvoiceLineDescription;
	private Long cInvoiceLineQtyEntered;
	private String cInvoiceLineCUOMId = "Each";
	private Long cInvoiceLinePriceEntered;
	private Long cInvoiceLinePriceList;
	private String cInvoiceLineCTaxId = "IVA SERVICIO 19%";
	private Long idType;
	
	@Override
	public String toString() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return adOrgId + ", " + documentNo + ", " + description + ", " + cDocTypeTargetId + ", " + sf.format(dateInvoiced) + ", " 
				+ sf.format(dateAcct) + ", " + cBPartnerId + ", " + adUserId + ", " + mPriceListId + ", " + salesRepId + ", "
				+ paymentRule + ", " + cPaymentTermId + ", " + cProjectId + ", " + cActivityId + ", " + cInvoiceLineAdOrgId + ", "
				+ cInvoiceLineCInvoiceId + ", " + cInvoiceLine + ", " + cInvoiceLineMProductId + ", " + cInvoiceLineDescription + ", "
				+ cInvoiceLineQtyEntered + ", " + cInvoiceLineCUOMId + ", " + cInvoiceLinePriceEntered + ", " + cInvoiceLinePriceList +
				", " + cInvoiceLineCTaxId + ", " + idType;
	}
	
	
}
