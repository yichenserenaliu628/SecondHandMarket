package com.example.secondhandmarketwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orderitem")
public class OrderItem  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private double price;
	@ManyToOne
	private Post post;


	private int quantity;

	@ManyToOne
	@JsonIgnore
	private Cart cart;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}


	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int i) {
	}


}
