package com.jtc.app.primary.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alliance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Alliance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alliance_id")
	private Long allianceId;
	@Column(name = "alliance_name")
	private String name;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "alliance")
	private Set<Client> clients;
	
}
