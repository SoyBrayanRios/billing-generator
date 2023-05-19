package com.jtc.app.primary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos de cada línea que compone una factura del archivo plano que se sube a Faceldi 
 * y a SMART.
 * Estos datos se encuentran almacenados en la tabla "bill_product" de la base de datos.
 *
 */
@Entity
@Table(name = "bill_product")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BillProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "bill", nullable = false)
	private Bill bill;
	@ManyToOne
	@JoinColumn(name = "product", nullable = false)
	private Product product;
	@Column(name = "quantity")
	private Long quantity;
	@Column(name = "price")
	private Long price;
}
