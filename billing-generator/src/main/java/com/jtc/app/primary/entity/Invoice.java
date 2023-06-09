package com.jtc.app.primary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos básicos de las facturas que se encuentran en Faceldi 
 * y que fueron almacenadas en la base de datos de Kosmos.
 * Estos datos se encuentran almacenados en la tabla "invoice" de la base de datos.
 *
 */
@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id")
	private Long invoiceId;
	@Column(name = "invoice_number")
	private String invoiceNumber;
	@Column(name = "document_type")
	private String documentType;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
	@Column(name = "issued_date")
	private Date issuedDate;
	@Column(name = "counted")
	private Boolean counted;
}
