package com.retail.demo.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class Customer {
	private long id;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Phone Number cannot be empty")
	private String phoneNumber;
	
	private int pointsBalance;

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	private String email;
	
	private String address;
	
	private boolean exist;
	
	private Date registrationDate;
	
	private List<Purchase> purchaseList;

	public Customer(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.pointsBalance = 0;
	}
	
	public Customer() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getPointsBalance() {
		return pointsBalance;
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

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPointsBalance(int pointsBalance) {
		this.pointsBalance = pointsBalance;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<Purchase> getPurchaseList() {
		return purchaseList;
	}

	public void setPurchaseList(List<Purchase> purchaseList) {
		this.purchaseList = purchaseList;
	}

	public void addPoints(int points) {
		if (points > 0)
			this.pointsBalance += points;
	}

	public void deductPoints(int points) {
		if (points <= this.pointsBalance)
			this.pointsBalance -= points;
	}
}
