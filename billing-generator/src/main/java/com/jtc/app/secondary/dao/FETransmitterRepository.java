package com.jtc.app.secondary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.secondary.entity.FETransmitter;

public interface FETransmitterRepository extends JpaRepository<FETransmitter, Long> {

	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('FV', 'NC', 'ND', 'FE', 'FCD', 'FCF'))", nativeQuery = true)
	public List<FETransmitter> getActiveFeTransmitters();
	
	@Query(value = "select * from sucursales \n"
			+ "where id_sucursal in (select distinct(id_sucursal_emisor) "
			+ "from documentos where tipo_documento in ('DS', 'NAS'))", nativeQuery = true)
	public List<FETransmitter> getActiveDsTransmitters();
	
}
