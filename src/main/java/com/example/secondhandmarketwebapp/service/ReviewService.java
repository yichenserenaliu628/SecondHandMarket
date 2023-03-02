package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.ReviewDao;
import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.request.ReviewSellerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private UserService userService;
    public void addReview(ReviewSellerRequest reviewSellerRequest){
        String sellerName = reviewSellerRequest.getSellerUserName();
        User seller = userService.getUserByUserName(sellerName);
        reviewDao.addReview(seller, reviewSellerRequest);
    }


}