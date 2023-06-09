package com.jtc.app.primary.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos básicos necesarios de los clientes de facturación, nómina y 
 * documento soporte que se requieren para generar los archivos planos de Faceldi y SMART.
 * Estos datos se encuentran almacenados en la tabla "client" de la base de datos.
 *
 */
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {

	@Id
	@Column(name = "nit")
	private String nit;
	@Column(name = "dv")
	private String dV;
	@Column(name = "tipo_identificacion")
	private Long tipoIdentificacion;
	@Column(name = "tipo_persona")
	private Long tipoPersona;
	@Column(name = "tipo_regimen")
	private Long tipoRegimen;
	@Column(name = "nombre_razon_social")
	private String razonSocial;
	@Column(name = "primer_apellido")
	private String primerApellido;
	@Column(name = "segundo_apellido")
	private String segundoApellido;
	@Column(name = "primer_nombre")
	private String primerNombre;
	@Column(name = "segundo_nombre")
	private String segundoNombre;
	@Column(name = "departamento")
	private String departamento;
	@Column(name = "municipio")
	private String municipio;
	@Column(name = "direccion")
	private String direccion;
	@Column(name = "zona_postal")
	private String zonaPostal;
	@Column(name = "correo_electronico")
	private String correoElectronico;
	@Column(name = "documento_equivalente")
	private Boolean documentoEquivalente;
	@ManyToOne
	@JoinColumn(name = "alliance_id", nullable = true)
	private Alliance alliance;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
	private Set<Branch> branches;
	
}
