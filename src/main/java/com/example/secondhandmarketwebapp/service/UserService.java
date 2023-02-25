package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.CartDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.Review;

import java.util.ArrayList;
import java.util.List;

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
    public void signUp(User user) {
        Cart cart = new Cart();
        user.setCart(cart);

        List<Post> post = new ArrayList<>();
        user.setPostList(post);

        List<Review> review = new ArrayList<>();
        user.setReviewList(review);

        user.setEnabled(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.signUp(user);
    }

    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }



}
