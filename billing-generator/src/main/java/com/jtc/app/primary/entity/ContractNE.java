package com.jtc.app.primary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.jtc.app.model.BaseContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contract_ne")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ContractNE extends BaseContract {

	@Column(name = "first_month_free")
	private Boolean firstMonthFree;
	@Column(name = "first_year_free")
	private Boolean firstYearFree;
	@Column(name = "discount_second_year")
	private Long discountSecondYear;
	@Column(name = "implementation_cost")
	private Long implementationCost;
	@Column(name = "implementation_already_paid")
	private Boolean implementationAlreadyPaid;
	@Column(name = "qualification_date")
	private Date qualificationDate;
	@Column(name = "charge_date")
	private Date chargeDate;

}
