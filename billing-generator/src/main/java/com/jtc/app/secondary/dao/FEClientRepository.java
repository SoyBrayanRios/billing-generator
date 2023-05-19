package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.secondary.entity.NEClient;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "clientes".
 *
 */
public interface FEClientRepository extends JpaRepository<FEClient, String> {

	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todos los clientes que sean emisores.
	 * @return Lista con los objetos que representan a los clientes.
	 */
	@Query(value = "select * from clientes where emisor;", nativeQuery = true)
	public List<FEClient> getActiveFEClients();
	
}
