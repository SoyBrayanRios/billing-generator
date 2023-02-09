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

@Entity
@Table(name = "branch")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Branch {

	@Id
	@Column(name = "branch_id")
	private Long branchId;
	@Column(name = "codigo")
	private String code;
	@Column(name = "branch_name")
	private String name;
	@Column(name = "country")
	private String country;
	@Column(name = "department")
	private Long department;
	@Column(name = "department_name")
	private String departmentName;
	@Column(name = "state")
	private Long state;
	@Column(name = "state_name")
	private String stateName;
	@Column(name = "center")
	private Long center;
	@Column(name = "address")
	private String address;
	@Column(name = "address_complement")
	private String addressComplement;
	@Column(name = "phone")
	private String phone;
	@Column(name = "email")
	private String email;
	@Column(name = "active", insertable = true, updatable = true)
	private Boolean active;
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "branch")
	private Set<Contract> contracts;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
	private Set<InvoiceResume> invoicesQuantity;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
	private Set<Invoice> invoices;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "branch")
	private Set<Bill> bills;
	
}
