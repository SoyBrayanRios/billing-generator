package com.jtc.app.secondary.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class NEInvoiceId implements Serializable {
	
	@Column(name = "id_sucursal_empleador")
	private Long id_sucursal_empleador;
	@Column(name = "tipo")
	private Long tipo;
	@Column(name = "numero")
	private String numero;
	
	public NEInvoiceId() {
	}

	public NEInvoiceId(Long id_sucursal_empleador, Long tipo, String numero) {
		super();
		this.id_sucursal_empleador = id_sucursal_empleador;
		this.tipo = tipo;
		this.numero = numero;
	}
	
}
