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
public class FilterItemController {
    @Autowired
    private PostService postService;

    @GetMapping("/pricelower/{maxPrice}")
    public ResponseEntity<List<PostResponse>> filterProductByMaxPrice(@PathVariable Double maxPrice) {
        try {
            List<PostResponse> products = postService.filterProductByMaxPrice(maxPrice);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pricerange/{minPrice},{maxPrice}")
    public ResponseEntity<List<PostResponse>> filterProductByPriceRange(@PathVariable Double minPrice, @PathVariable Double maxPrice) {
        try {
            List<PostResponse> products = postService.filterProductByPriceRange(minPrice, maxPrice);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/filter/{category}")
    public ResponseEntity<List<PostResponse>> filterProductByCategory(@PathVariable String category) {
        try {
            List<PostResponse> products = postService.filterProductByCategory(category);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filterBySellerRating/{minRating}")
    public ResponseEntity<List<PostResponse>> filterProductBySellerRating(@PathVariable Double minRating) {
        try {
            List<PostResponse> products = postService.filterProductBySellerRating(minRating);
            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
