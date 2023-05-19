package com.jtc.app.primary.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta clase define un objeto que contiene los datos de cancelaciones de contratos para que no se 
 * generen cobros al calcular la facturación.
 * Estos datos se encuentran almacenados en la tabla "cancellation" de la base de datos.
 *
 */
@Entity
@Table(name = "cancellation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContractCancellation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long cancellationId;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
	private Contract contract;
	@Column(name = "date")
	private Date date;
	@Column(name = "description")
	private String description;
}
