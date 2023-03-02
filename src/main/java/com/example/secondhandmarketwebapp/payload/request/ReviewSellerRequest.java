package com.example.secondhandmarketwebapp.payload.request;

import com.example.secondhandmarketwebapp.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ReviewSellerRequest {
    @NotNull
    private String sellerUserName;
    @NotBlank
    private double rating;
    private String comment;

}
