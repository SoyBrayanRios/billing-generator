package com.jtc.app.primary.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "custody")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Custody {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "custody_id")
	private Long custodyId;
	@Column(name = "custody_cost")
	private double custodyCost;
	@ManyToOne
	@JoinColumn(name = "frequency_id", nullable = false)
	private Frequency custodyFrequency;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "custodyType")
	private Set<ContractFE> contracts;
}
