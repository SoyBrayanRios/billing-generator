package com.jtc.app.secondary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Esta clase define un objeto que contiene los datos básicos necesarios de los clientes de nómina 
 * almacenados en la base de datos de producción de Faceldi
 * Estos datos se encuentran almacenados en la tabla "ne_empleadores" de la base de datos.
 *
 */
@Entity
@Table(name = "ne_empleadores")
@Data
public class NEClient {

	@Id
	@Column(name = "nro_identificacion")
	private String nroIdentificacion;
	@Column(name = "tipo_identificacion")
	private Long tipoIdentificacion;
	@Column(name = "digito_verificacion")
	private String digitoVerif;
	@Column(name = "razon_social")
	private String razonSocial;
	@Column(name = "primer_apellido")
	private String primerApellido;
	@Column(name = "segundo_apellido")
	private String segundoApellido;
	@Column(name = "primer_nombre")
	private String primerNombre;
	@Column(name = "segundo_nombre")
	private String segundoNombre;
	@Column(name = "estado")
	private Long estado;
	@Column(name = "departamento")
	private String departamento;
	@Column(name = "municipio")
	private String municipio;
	@Column(name = "direccion")
	private String direccion;
	@Column(name = "correo_electronico")
	private String correoElectronico;
	@Column(name = "alianza_comercial")
	private Long alianzaComercial;

}
