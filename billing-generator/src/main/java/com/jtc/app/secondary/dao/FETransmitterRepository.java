package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.Transmitter;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "sucursales".
 *
 */
public interface FETransmitterRepository extends JpaRepository<Transmitter, Long> {

	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener las sucursales emisoras de FE.
	 * @return Lista con los objetos que representan a las sucursales emisoras de FE.
	 */
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('FV', 'NC', 'ND', 'FE', 'FCD', 'FCF'))", nativeQuery = true)
	public List<Transmitter> getActiveFeTransmitters();
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener las sucursales emisoras de DS.
	 * @return Lista con los objetos que representan a las sucursales emisoras de DS.
	 */
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('DS', 'NAS'))", nativeQuery = true)
	public List<Transmitter> getActiveDsTransmitters();
	
	/**
	 * Consulta a la base de datos de producción de Faceldi para obtener las sucursales emisoras de NE.
	 * @return Lista con los objetos que representan a las sucursales emisoras de NE.
	 */
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_empleador) from ne_documentos)", nativeQuery = true)
	public List<Transmitter> getActiveNeTransmitters();
	
}
