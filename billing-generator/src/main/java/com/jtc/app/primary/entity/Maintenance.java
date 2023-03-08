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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "maintenance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Maintenance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "maintenance_id")
	private Long maintenanceId;
	@Column(name = "maintenance_cost")
	private Long maintenanceCost;
	@ManyToOne
	@JoinColumn(name = "frequency_id", nullable = false)
	private Frequency maintenanceFrequency;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "maintenanceType")
	private Set<ContractFE> contracts;
	
}
