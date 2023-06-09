package com.jtc.app.secondary.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Esta clase define un objeto que contiene los datos básicos de las facturas de nómina que se encuentran almacenadas en
 * la base de datos de producción de Faceldi.
 * Estos datos se encuentran almacenados en la tabla "ne_documentos" de la base de datos.
 *
 */
@Entity
@Table(name = "ne_documentos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
