package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.exception.CheckoutException;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.CartService;
import com.example.secondhandmarketwebapp.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Set;
// to do fix bug
@Controller
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private CartService cartService;
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> checkout() throws CheckoutException{
        try {
            Set<String> sellerUserNames = cartService.checkOut();
            return new ResponseEntity<>(sellerUserNames, HttpStatus.OK);
        } catch (CheckoutException e) {
            logger.error("Failed to add new product " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getErrorMessage());
        }
    }
}