package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.ReviewDao;
import com.example.secondhandmarketwebapp.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewDao reviewDao;
    public void addReview(List<Review> reviewList){
        for(Review review : reviewList){
            reviewDao.addReview(review);
        }
    }
}
