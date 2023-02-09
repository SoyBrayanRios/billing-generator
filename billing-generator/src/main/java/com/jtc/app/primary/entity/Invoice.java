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
	//@Column(name = "buyer_id")
	//private Long buyerId;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
	@Column(name = "issued_date")
	private Date issuedDate;
	@Column(name = "counted")
	private Boolean counted;
}
