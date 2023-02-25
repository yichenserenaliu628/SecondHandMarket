package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SortItemController {
    @Autowired
    private PostService postService;
    @GetMapping("/products/sort/price")
    public ResponseEntity<List<PostResponse>> sortProductByPriceLowToHigh() {
        try {
            List<PostResponse> products = postService.sortProductByPriceLowToHigh();
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/sort/pricehightolow")
    public ResponseEntity<List<PostResponse>> sortProductByPriceHighToLow() {
        try {
            List<PostResponse> products = postService.sortProductByPriceHighToLow();
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
