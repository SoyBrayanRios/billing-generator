package com.jtc.app.secondary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sucursales")
@Data
public class Transmitter {

	@Id
	@Column(name = "id_sucursal")
	private Long idSucursal;
	@Column(name = "nro_identificacion")
	private String nroIdentificacion;
	@Column(name = "codigo")
	private String codigo;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "pais")
	private String pais;
	@Column(name = "departamento")
	private Long departamento;
	@Column(name = "municipio")
	private Long municipio;
	@Column(name = "centro")
	private Long centro;
	@Column(name = "direccion")
	private String direccion;
	@Column(name = "complemento_direccion")
	private String complementoDireccion;
	@Column(name = "telefono")
	private String telefono;
	@Column(name = "correo_electronico")
	private String correoElectronico;
	@Column(name = "estado")
	private Long estado;
	@Column(name = "nombre_departamento")
	private String nombreDepartamento;
	@Column(name = "nombre_municipio")
	private String nombreMunicipio;
}
