package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "frequency".
 *
 */
public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

	/**
	 * Consulta a la base de datos de Kosmos para obtener los datos de un tipo de frecuencia según su ID.
	 * @param frequencyId (ID del tipo de frecuencia).
	 * @return El objeto con los datos del tipo de frecuencia que corresponda al ID suministrado.
	 */
	@Query(value = "select * from frequency where frequency_id=:frequencyId", nativeQuery = true)
	public Frequency getFrequencyById(Long frequencyId);
}
