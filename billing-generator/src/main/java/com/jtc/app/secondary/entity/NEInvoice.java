package com.jtc.app.secondary.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ne_documentos")
@Data
public class NEInvoice {

	@EmbeddedId
	private NEInvoiceId neInvoiceId;
	@Column(name = "tipo_identificacion_trabajador")
	private Long tipo_identificacion_trabajador;
	@Column(name = "numero_identificacion_trabajador")
	private String numero_identificacion_trabajador;
	@Column(name = "codigo_contrato")
	private String codigo_contrato;
	@Column(name = "raw")
	private String raw;
	
}
