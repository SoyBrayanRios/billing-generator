package com.jtc.app.primary.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jtc.app.model.BaseContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contract_fe")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ContractFE extends BaseContract {

	@ManyToOne
	@JoinColumn(name = "maintenance_id", nullable = true)
	private Maintenance maintenanceType;
	@Column(name = "shared_maintenance")
	private Boolean sharedMaintenance;
	@Column(name = "maintenance_already_paid")
	private Boolean maintenanceAlreadyPaid;
	@ManyToOne
	@JoinColumn(name = "custody_id", nullable = true)
	private Custody custodyType;

}
