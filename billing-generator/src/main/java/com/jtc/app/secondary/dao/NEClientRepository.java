package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.secondary.entity.NEClient;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "ne_empleadores".
 *
 */
public interface NEClientRepository extends JpaRepository<NEClient, String> {
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener todos los clientes que sean emisores de nómina.
	 * @return Lista con los objetos que representan a los clientes emisores de nómina.
	 */
	@Query(value = "select * from ne_empleadores", nativeQuery = true)
	public List<NEClient> getActiveNEClients();
	
}
