package com.jtc.app.primary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contract")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Contract {

	@Id
	@Column(name = "contract_id")
	private String contractId;
	@Column(name = "contract_date")
	private Date contractDate;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "implementation_cost")
	private Long implementationCost;
	@Column(name = "shared_contract")
	private Boolean sharedContract;
	@Column(name = "shared_maintenance")
	private Boolean sharedMaintenance;
	@Column(name = "shared_contract_id")
	private String sharedContractId;
	@Column(name = "ipc_increase")
	private Boolean ipcIncrease;
	@Column(name = "first_issue_date")
	private Date firstIssueDate;
	@Column(name = "prepaid")
	private Boolean prepaid;
	@Column(name = "implementation_already_paid")
	private Boolean implementationAlreadyPaid;
	@Column(name = "maintenance_already_paid")
	private Boolean maintenanceAlreadyPaid;
	@ManyToOne
	@JoinColumn(name = "payment_plan", nullable = true)
	private PaymentType paymentPlan;
	@ManyToOne
	@JoinColumn(name = "maintenance_id", nullable = true)
	private Maintenance maintenanceType;
	@ManyToOne
	@JoinColumn(name = "custody_id", nullable = true)
	private Custody custodyType;
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;

}