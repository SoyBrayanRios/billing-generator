package com.jtc.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.jtc.app.primary.entity.Branch;
import com.jtc.app.primary.entity.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseContract {
	
	@Id
	@Column(name = "contract_id")
	private String contractId;
	@Column(name = "contract_date")
	private Date contractDate;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "shared_contract")
	private Boolean sharedContract;
	@Column(name = "shared_contract_id")
	private String sharedContractId;
	@Column(name = "ipc_increase")
	private Boolean ipcIncrease;
	@Column(name = "first_issue_date")
	private Date firstIssueDate;
	@Column(name = "implementation_cost")
	private Long implementationCost;
	@Column(name = "implementation_already_paid")
	private Boolean implementationAlreadyPaid;
	@Column(name = "prepaid")
	private Boolean prepaid;
	@Column(name = "reference_payment_date")
	private Date referencePaymentDate;
	@ManyToOne
	@JoinColumn(name = "payment_plan", nullable = true)
	private PaymentType paymentPlan;
	@ManyToOne
	@JoinColumn(name = "branch_id", nullable = false)
	private Branch branch;
}
