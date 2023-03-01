package com.example.secondhandmarketwebapp.payload.response;

import com.example.secondhandmarketwebapp.entity.ProductImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Setter
@Getter
public class PostResponse {
    int id;
    String title;
    double price;
    String description;
    String zipcode;
    int quantity;
    String category;
    boolean isSold;
    String sellerEmail;
    double sellerRating;
    String uuid;
}