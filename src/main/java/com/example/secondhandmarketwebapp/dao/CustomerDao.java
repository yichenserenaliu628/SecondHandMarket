package com.example.secondhandmarketwebapp.dao;


import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao {
    public void signUp(User customer) {
    }

    public User getCustomer(String email) {
        return new User();
    }

}
