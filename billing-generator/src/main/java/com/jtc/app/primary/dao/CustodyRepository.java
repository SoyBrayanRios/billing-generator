package com.jtc.app.primary.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtc.app.primary.entity.Custody;

public interface CustodyRepository extends JpaRepository<Custody, Long> {

}
