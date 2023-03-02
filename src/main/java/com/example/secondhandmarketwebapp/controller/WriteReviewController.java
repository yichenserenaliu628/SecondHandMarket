package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.payload.request.ReviewSellerRequest;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.MessageResponse;
import com.example.secondhandmarketwebapp.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class WriteReviewController {
    @Autowired
    private ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(WriteReviewController.class);
    // TO DO
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> writeReview(@RequestParam("sellerUserName") String sellerUserName,
                            @RequestParam("rating") double rating,
                            @RequestParam("comment") String comment){
        try {
            // create review request
            ReviewSellerRequest reviewSellerRequest = new ReviewSellerRequest();
            reviewSellerRequest.setSellerUserName(sellerUserName);
            reviewSellerRequest.setRating(rating);
            reviewSellerRequest.setComment(comment);
            reviewService.addReview(reviewSellerRequest);
            return ResponseEntity.ok(new MessageResponse("Seller review added successfully!"));
        } catch (RuntimeException e) {
            logger.error("Failed to review seller " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }
}