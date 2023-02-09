package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.Alliance;

public interface AllianceRepository extends JpaRepository<Alliance, Long> {
	
	@Query(value = "select * from alliance where alliance_id=:allianceId", nativeQuery = true)
	public Alliance findByAllianceId(Long allianceId);
	public Alliance findByName(String allianceName);

}
