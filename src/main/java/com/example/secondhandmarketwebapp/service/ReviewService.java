package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.ReviewDao;
import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.request.ReviewSellerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private UserService userService;
    public void addReview(String sellerUserName, double rating, String comment){

        User seller = userService.getUserByUserName(sellerUserName);
        reviewDao.addReview(seller, rating, comment);
    }




}