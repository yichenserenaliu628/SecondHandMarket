package com.example.secondhandmarketwebapp.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Setter
@Getter
public class PostResponse {
    int postId;
    String title;
    double price;
    String description;
    String zipcode;
    String uuid;
}