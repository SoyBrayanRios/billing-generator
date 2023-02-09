package com.jtc.app.primary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_resume")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceResume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "resume_id")
	private Long resumeId;
	@Column(name = "year")
	private Long year;
	@Column(name = "month")
	private Long month;
	@Column(name = "issued_invoices")
	private Long issuedInvoices;
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
	@ManyToOne
	@JoinColumn(name = "bill_id", nullable = true)
	private Bill bill;
}
