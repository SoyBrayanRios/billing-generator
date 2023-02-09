package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {
	
	@Query(value = "select * from client where nit=:nit", nativeQuery = true)
	public Client findByNit(String nit);

}
