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
	
	private static final String adOrgId = "JAIME TORRES C Y CIA SA";
	private Long documentNo;
	private String description;
	private static final String cDocTypeTargetId = "Factura CXC";
	private Date dateInvoiced;
	private Date dateAcct;
	private String cBPartnerId;
	private static final String adUserId = "";
	private static final String mPriceListId = "FAC ELECTRONICA";
	private static final String salesRepId = "jaimeAdmin";
	private static final String paymentRule = "";
	private static final String cPaymentTermId = "1000002";
	private static final Integer cProjectId = 7;
	private Integer cActivityId;
	private static final String cInvoiceLineAdOrgId = "JAIME TORRES C Y CIA SA";
	private Long cInvoiceLineCInvoiceId;
	private Integer cInvoiceLine;
	private Long cInvoiceLineMProductId;
	private String cInvoiceLineDescription;
	private Long cInvoiceLineQtyEntered;
	private static final String cInvoiceLineCUOMId = "Each";
	private Long cInvoiceLinePriceEntered;
	private Long cInvoiceLinePriceList;
	private static final String cInvoiceLineCTaxId = "IVA SERVICIO 19%";
	public static String getAdorgid() {
		return adOrgId;
	}
	public static String getCdoctypetargetid() {
		return cDocTypeTargetId;
	}
	public static String getAduserid() {
		return adUserId;
	}
	public static String getMpricelistid() {
		return mPriceListId;
	}
	public static String getSalesrepid() {
		return salesRepId;
	}
	public static String getPaymentrule() {
		return paymentRule;
	}
	public static String getCpaymenttermid() {
		return cPaymentTermId;
	}
	public static Integer getCprojectid() {
		return cProjectId;
	}
	public static String getCinvoicelineadorgid() {
		return cInvoiceLineAdOrgId;
	}
	public static String getCinvoicelinecuomid() {
		return cInvoiceLineCUOMId;
	}
	public static String getCinvoicelinectaxid() {
		return cInvoiceLineCTaxId;
	}
	@Override
	public String toString() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return adOrgId + ", " + documentNo + ", " + description + ", " + cDocTypeTargetId + ", " + sf.format(dateInvoiced) + ", " 
				+ sf.format(dateAcct) + ", " + cBPartnerId + ", " + adUserId + ", " + mPriceListId + ", " + salesRepId + ", "
				+ paymentRule + ", " + cPaymentTermId + ", " + cProjectId + ", " + cActivityId + ", " + cInvoiceLineAdOrgId + ", "
				+ cInvoiceLineCInvoiceId + ", " + cInvoiceLine + ", " + cInvoiceLineMProductId + ", " + cInvoiceLineDescription + ", "
				+ cInvoiceLineQtyEntered + ", " + cInvoiceLineCUOMId + ", " + cInvoiceLinePriceEntered + ", " + cInvoiceLinePriceList +
				", " + cInvoiceLineCTaxId;
	}
	
	
}
