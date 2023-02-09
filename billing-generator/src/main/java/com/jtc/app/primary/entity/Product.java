package com.jtc.app.primary.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	
	@Id
	@Column(name = "product_id")
	private Long productId;
	@Column(name = "product_name")
	private String description;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY , mappedBy = "product")
	private Set<BillProduct> billProducts;
}
