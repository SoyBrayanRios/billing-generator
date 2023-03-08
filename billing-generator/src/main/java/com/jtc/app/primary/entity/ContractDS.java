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
@Table(name = "contract_ds")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContractDS extends BaseContract {

	@Column(name = "reference_payment_date")
	private Date referencePaymentDate;
	
}
