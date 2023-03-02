package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.CartDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.Review;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserDao userDao;

    //todo
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao customerDao, PasswordEncoder passwordEncoder) {
        this.userDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    //Todo edit
    public ResponseEntity<String> signUp(@Valid User user) throws InvalidUserException {

        Cart cart = new Cart();
        user.setCart(cart);

        List<Post> post = new ArrayList<>();
        user.setPostList(post);

        List<Review> review = new ArrayList<>();
        user.setReviewList(review);

        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            throw new InvalidUserException("Email is invalid");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty() || !isValidUserName(user.getUsername())) {
            throw new InvalidUserException("Username is invalid.");
        }
        if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
            throw new InvalidUserException("First name is required");
        }
        if (user.getLastname() == null || user.getLastname().isEmpty()) {
            throw new InvalidUserException("Last name is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidUserException("Password is required");
        }
        if (user.getPhone() == null || !isValidPhoneNumber(user.getPhone())) {
            throw new InvalidUserException("Phone number is invalid");
        }
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new InvalidUserException("Address is required");
        }
        userDao.signUp(user);
        return ResponseEntity.ok("User created successfully");
    }

    private boolean isValidUserName(String username) {
        return userDao.isValidUserName(username);
    }

    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    public boolean isValidEmail(String email) {
        // Regular expression pattern to match email addresses
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // Create a pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a matcher object with the given email string
        Matcher matcher = pattern.matcher(email);

        // Return true if the email string matches the pattern
        return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String pattern = "\\(\\d{3}\\)\\d{3}-\\d{4}";
        return Pattern.matches(pattern, phoneNumber);
    }
}