package com.retail.demo.dto.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(name="uc_phone_number", columnNames = {"phone_number"}))
public class CustomerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "phone_number")   
	private String phoneNumber;
    
    @Column(name = "points_balance")
	private int pointsBalance;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "address")
	private String address;
    
    @Column(name = "registration_date")
    private Date registrationDate;
    
    @OneToMany
    @JoinColumn(name = "customerId")
    List<PurchaseEntity> purchaseList;

   
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPointsBalance() {
		return pointsBalance;
	}

	public void setPointsBalance(int pointsBalance) {
		this.pointsBalance = pointsBalance;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<PurchaseEntity> getPurchaseList() {
		return purchaseList;
	}

	public void setPurchaseList(List<PurchaseEntity> purchaseList) {
		this.purchaseList = purchaseList;
	}
}
