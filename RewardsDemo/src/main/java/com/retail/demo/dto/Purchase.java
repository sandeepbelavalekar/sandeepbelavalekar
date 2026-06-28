package com.retail.demo.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class Purchase {

	@JsonIgnore
	private int id;

	@NotEmpty(message = "Item must be added")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Item> itemList;

	private double totalPrice;
	
	private Date purchaseDate;
	
	private long customerId;
	
	private int points;
	
	@NotBlank(message = "Phone Number cannot be empty")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String phoneNumber;	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
