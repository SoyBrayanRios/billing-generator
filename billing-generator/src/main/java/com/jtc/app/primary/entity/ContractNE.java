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
	@ManyToOne
	@JoinColumn(name = "custody_id", nullable = true)
	private Custody custodyType;

}
