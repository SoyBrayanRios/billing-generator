package com.jtc.app.primary.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos de una frequencia de cobro. Ejm: mensual, anual, etc.
 * Estos datos se encuentran almacenados en la tabla "frequency" de la base de datos.
 *
 */
@Entity
@Table(name = "frequency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Frequency {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "frequency_id")
	private Long frequencyId;
	@Column(name = "frequency_name")
	private String frequencyName;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentFrequency")
	private Set<PaymentType> packageTypes;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "maintenanceFrequency")
	private Set<Maintenance> maintenances;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "custodyFrequency")
	private Set<Custody> custodies;

}
