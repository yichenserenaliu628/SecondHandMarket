package com.example.secondhandmarketwebapp.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "cart")
public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(unique = true)
	private User user;
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<OrderItem> orderItemList;
	private double totalPrice;

}