package com.infosys.retail.dto.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase")
public class PurchaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "totalPrice")
	private double totalPrice;

	@Column(name = "purchaseDate")
	private Date purchaseDate;

	@Column(name = "customerId")
	private long customerId;
	
	@Column(name = "points")
	private double points;

	public int getId() {
		return id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

}
