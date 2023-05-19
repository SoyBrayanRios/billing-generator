package com.jtc.app.secondary.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * Esta clase define un objeto que contiene los datos de la llave primaria de la tabla "ne_documentos" y
 * que se encuentra embebido en la clase NEInvoice.
 *
 */
@Embeddable
@Data
public class NEInvoiceId implements Serializable {
	
	@Column(name = "id_sucursal_empleador")
	private Long id_sucursal_empleador;
	@Column(name = "tipo")
	private Long tipo;
	@Column(name = "numero")
	private String numero;
	
	/**
	 * Método contructor vacío de la llave primaria.
	 */
	public NEInvoiceId() {
	}

	/**
	 * Método contructor de la llave primaria de la factura.
	 * @param id_sucursal_empleador (ID de la sucursal emisora).
	 * @param tipo (Tipo de documento).
	 * @param numero (Número del documento).
	 */
	public NEInvoiceId(Long id_sucursal_empleador, Long tipo, String numero) {
		super();
		this.id_sucursal_empleador = id_sucursal_empleador;
		this.tipo = tipo;
		this.numero = numero;
	}
	
}
