package com.jtc.app.primary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene la información general de cada ejecución de facturación 
 * que se haya guardado en la base de datos de Kosmos, indicando el año, mes, No. factura inicial y final 
 * junto con el módulo al que pertenece esa ejecución.
 * Estos datos se encuentran almacenados en la tabla "bill_resume" de la base de datos.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill_resume")
@Getter
@Setter
public class BillDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "year")
	private Long year;
	@Column(name = "month")
	private Long month;
	@Column(name = "initial_invoice")
	private Long initialInvoice;
	@Column(name = "final_invoice")
	private Long finalInvoice;
	@Column(name = "module")
	private String module;
}
