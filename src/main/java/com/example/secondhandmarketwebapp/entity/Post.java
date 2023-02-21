package com.example.secondhandmarketwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "menuitem")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	private int zipcode;
	private String description;
	private double price;
	private String imageUrl;
	private boolean status;
	private String categories;
	@ManyToOne
	@JsonIgnore
	private User user;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(unique = true)
	private OrderItem orderItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
}
