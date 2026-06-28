package com.retail.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.demo.dto.entity.PurchaseEntity;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Integer> {
	
	public List<PurchaseEntity> findByCustomerId(long customerId); 

}
