package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao customerDao, PasswordEncoder passwordEncoder) {
        this.userDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(User user) {
        Cart cart = new Cart();
        user.setCart(cart);

        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.signUp(user);
    }

    public User getUser(String email) {
        return userDao.getCustomer(email);
    }


}
