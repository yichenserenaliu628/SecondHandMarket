package com.example.secondhandmarketwebapp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.example.secondhandmarketwebapp.entity.Post;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Email(message = "Email should be valid")
	private String email;
	@NotNull(message = "First Name cannot be null")
	@Size(min=1, max=16)
	private String firstname;

	@NotNull(message = "Last Name cannot be null")
	@Size(min=1, max=16)
	private String lastname;

	@NotBlank
	@Size(max = 120)
	private String password;

	@NotBlank
	@Pattern(regexp="\\(\\d{3}\\)\\d{3}-\\d{4}")
	private String phone;
	@NotBlank
	@Size(min = 6, max = 10, message
			= "Username must be between 6 and 10 characters")
	private String username;
	@NotBlank
	@Size(max = 150, message
			= "Address must not be longer than 150 characters")
	private String address;

	@AssertTrue
	private boolean isEnabled;
	//private String imageUrl
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL /*, fetch = FetchType.EAGER*/)
	private List<Post> postList;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Review> reviewList;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(unique = true)
	private Cart cart;
}