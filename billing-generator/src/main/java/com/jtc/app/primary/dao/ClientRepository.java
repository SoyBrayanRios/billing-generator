package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Client;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "client".
 *
 */
public interface ClientRepository extends JpaRepository<Client, String> {
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener un cliente según su NIT.
	 * @param nit (NIT de la empresa).
	 * @return El objeto con los datos del cliente que corresponda al NIT suministrado.
	 */
	@Query(value = "select * from client where nit=:nit", nativeQuery = true)
	public Client findByNit(String nit);

}
