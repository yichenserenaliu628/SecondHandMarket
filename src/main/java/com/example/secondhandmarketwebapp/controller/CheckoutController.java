package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Controller
public class CheckoutController {
    @Autowired
    private CartService cartService;
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String checkout() {
        if (!cartService.stockSufficient()) {
            // TODO: how to report error message to users
            System.out.println("手慢了 or stock not sufficient");
        } else {
            Set<Integer> sellerList = cartService.cleanCart();
            // test passed. sellerId 可以被打印出来 :)
            for (int sellerId : sellerList) {
                System.out.println(sellerId);
            }
        }
        return "redirect:/checkout?reviewuser";
    }
}
