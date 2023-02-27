package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class WriteReviewController {
    @Autowired
    private ReviewService reviewService;
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @ResponseBody
    public void writeReview(@RequestBody List<Review> reviewList){
        reviewService.addReview(reviewList);
    }
}
