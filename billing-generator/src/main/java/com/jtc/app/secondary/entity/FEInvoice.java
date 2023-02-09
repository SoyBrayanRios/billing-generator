package com.jtc.app.secondary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "documentos")
@Data
public class FEInvoice {

	@Id
	@Column(name = "numero_documento")
	private String invoiceNumber;
	@Column(name = "tipo_documento")
	private String documentType;
	@Column(name = "id_sucursal_emisor")
	private Long transmitterId;
	@Column(name = "prefijo_factura")
	private String prefix;
	@Column(name = "consecutivo_factura")
	private String consecutive;
	@Column(name = "cufe")
	private String cufe;
	@Column(name = "id_sucursal_adquiriente")
	private Long clientId;
	@Column(name = "tipo_documento_factura")
	private Long docType;
	@Column(name = "divisa_documento")
	private String badgeType;
	@Column(name = "total_cargos")
	private Long totalCharge;
	@Column(name = "total_descuentos")
	private Long totalDiscount;
	@Column(name = "total_documento")
	private Long totalDoc;
	@Column(name = "total_base_importe")
	private Long totalBase;
	@Column(name = "total_importe_bruto")
	private Long totalNet;
	@Column(name = "total_importe_impuesto")
	private Long totalTax;
	@Column(name = "fecha_creacion")
	private Date creationDate;
	@Column(name = "estado")
	private int status;
	@Column(name = "estado_dian")
	private String dianStatus;
	@Column(name = "codigo_cliente")
	private String clientCode;
	@Column(name = "codigo_vendedor")
	private String sellerCode;
	@Column(name = "tipo_factura")
	private Long invoiceType;
	@Column(name = "fecha_hora_emision")
	private Date issueDate;
	@Column(name = "correo_adquiriente")
	private String buyerEmail;
}
