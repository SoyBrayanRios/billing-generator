package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.Transmitter;

public interface FETransmitterRepository extends JpaRepository<Transmitter, Long> {

	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('FV', 'NC', 'ND', 'FE', 'FCD', 'FCF'))", nativeQuery = true)
	public List<Transmitter> getActiveFeTransmitters();
	
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('DS', 'NAS'))", nativeQuery = true)
	public List<Transmitter> getActiveDsTransmitters();
	
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_empleador) from ne_documentos)", nativeQuery = true)
	public List<Transmitter> getActiveNeTransmitters();
	
}
