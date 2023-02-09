package com.jtc.app.primary.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bill")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Bill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_id")
	private Long billId;
	@Column(name = "bill_number")
	private Long billNumber;
	@Column(name = "prefix")
	private String prefix;
	@Column(name = "creation_date")
	private Date creationDate;
	@Column(name = "expiration_date")
	private Date expirationDate;
	@Column(name = "description", length = 500)
	private String description;
	@Column(name = "year")
	private Long year;
	@Column(name = "month")
	private Long month;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY , mappedBy = "product")
	private Set<BillProduct> billProducts;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY , mappedBy = "bill")
	private Set<InvoiceResume> invoiceResume;
	@ManyToOne
	@JoinColumn(name = "branch", nullable = false)
	private Branch branch; 
	
	
}
