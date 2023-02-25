package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GetCartController {
    @Autowired
    private CartService cartService;
    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public Cart getCart(@AuthenticationPrincipal UserDetails userDetails){
        System.out.println("getCart Break Point");
        return cartService.getCart(userDetails.getUsername());
    }
}
