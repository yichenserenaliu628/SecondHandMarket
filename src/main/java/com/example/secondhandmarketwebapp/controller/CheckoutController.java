package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.exception.CheckoutException;
import com.example.secondhandmarketwebapp.service.CartService;
import com.example.secondhandmarketwebapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

@Controller
public class CheckoutController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ReviewService reviewService;
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String checkout(Model model, RedirectAttributes redirectAttributes) {
        try {
            Set<String> sellerUsernames = cartService.checkOut();
            model.addAttribute("sellerIdLists", sellerUsernames);
            redirectAttributes.addFlashAttribute("sellerIdLists", sellerUsernames);
            return "redirect:/review";
        } catch (CheckoutException e) {
            model.addAttribute("error", "Checkout failed: " + e.getMessage());
            return "checkout";
        }
    }
}
