package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Frequency;

public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

	@Query(value = "select * from frequency where frequency_id=:frequencyId", nativeQuery = true)
	public Frequency getFrequencyById(Long frequencyId);
}
