package com.example.secondhandmarketwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class AddProductRequest {

    @NotBlank
    private int zipcode;

    @NotBlank
    private String description;

    @NotBlank
    private double price;

    @NotBlank
    private int quantity;

    @NotBlank
    private String title;
    @NotBlank
    private String category;

    private boolean isSold;

    @NotNull
    private String keyName;

    private String uuid;

}
