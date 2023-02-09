package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEClient;

public interface FEClientRepository extends JpaRepository<FEClient, String> {

	@Query(value = "select * from clientes where emisor;", nativeQuery = true)
	public List<FEClient> getActiveFEClients();
	
}
