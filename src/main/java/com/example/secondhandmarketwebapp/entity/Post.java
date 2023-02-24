package com.example.secondhandmarketwebapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Table(name = "post")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank
	@Digits(integer = 5, fraction = 0)
	private int zipcode;
	@NotBlank
	@Size(max = 200, message
			= "Description should not be longer than 200 characters")
	private String description;

	@NotBlank
	@Min(0)
	private double price;
	//private String imageUrl;
	@NotBlank
	@Min(1)
	private int quantity;
	@NotBlank
	@Size(max = 20, message
			= "Product title should not be longer than 20 characters")
	private String title;
	@Column(name = "isSold", nullable = false, columnDefinition = "boolean default false")
	private boolean isSold;
	private String category;
	@ManyToOne
	@JsonIgnore
	private User user;
	@OneToMany(mappedBy ="post", cascade = CascadeType.ALL)
	private List<OrderItem> orderItem;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(unique = true)
	private ProductImage image;
}
