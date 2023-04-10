package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.secondary.entity.NEClient;

public interface NEClientRepository extends JpaRepository<NEClient, String> {
	
	@Query(value = "select * from ne_empleadores", nativeQuery = true)
	public List<NEClient> getActiveNEClients();
	
}
