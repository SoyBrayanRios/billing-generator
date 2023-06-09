package com.jtc.app.secondary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Esta clase define un objeto que contiene los datos básicos necesarios de los clientes de facturación y 
 * documento soporte almacenados en la base de datos de producción de Faceldi.
 * Estos datos se encuentran almacenados en la tabla "clientes" de la base de datos.
 *
 */
@Entity
@Table(name = "clientes")
@Data
public class FEClient {

	@Id
	@Column(name = "nro_identificacion")
	private String nroIdentificacion;
	@Column(name = "tipo_identificacion")
	private Long tipoIdentificacion;
	@Column(name = "digito_verif")
	private String digitoVerif;
	@Column(name = "tipo_regimen")
	private Long tipoRegimen;
	@Column(name = "tipo_persona")
	private Long tipoPersona;
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
	@Column(name = "emisor")
	private Boolean emisor;
	@Column(name = "estado")
	private Long estado;
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
	@Column(name = "alianza_comercial")
	private Long alianzaComercial;
	@Column(name = "documento_equivalente")
	private Boolean documentoEquivalente;
}
