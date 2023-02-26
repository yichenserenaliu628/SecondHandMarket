package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.CartDao;
import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartService {

    @Autowired
    private UserService userService;
    @Autowired
    private CartDao cartDao;

    public Cart getCart() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);

        if (user != null) {
            Cart cart = user.getCart();
            double totalPrice = 0;
            for (OrderItem item : cart.getOrderItemList()) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
            cart.setTotalPrice(totalPrice);
            return cart;
        }
        return new Cart();
    }

    public Set<Integer> cleanCart() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        if (user != null) return cartDao.removeAllCartItems(user.getCart());
        return null;
    }

    public boolean stockSufficient() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        return cartDao.stockSufficient(user.getCart().getOrderItemList());
    }

}
