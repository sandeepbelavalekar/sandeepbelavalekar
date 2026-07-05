package com.infosys.retail.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.retail.dto.entity.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {
	
	public List<PurchaseEntity> findByCustomerId(long customerId); 

}
