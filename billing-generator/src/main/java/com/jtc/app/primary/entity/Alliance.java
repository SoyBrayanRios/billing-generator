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
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos de una alianza tal como se encuentra en producción en Faceldi. 
 * Adicionalmente, se mapean su equivalencia con los centros de costo de SMART.
 * Estos datos se encuentran almacenados en la tabla "alliance" de la base de datos. 
 *
 */
@Entity
@Table(name = "alliance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Alliance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alliance_id")
	private Long allianceId;
	
	@Column(name = "alliance_name")
	private String name;
	
	@Column(name = "smart_cc")
	private Long smartCC;
	
	@Column(name = "smart_cc_name")
	private String ccName;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "alliance")
	private Set<Client> clients;
	
}
