package com.infosys.retail.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infosys.retail.dto.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
	
	@Query("from CustomerEntity c where c.phoneNumber=:phoneNumber")
	public CustomerEntity findByPhoneNumber(@Param(value = "phoneNumber") String phoneNumber);

}
