package com.example.secondhandmarketwebapp.service;

import com.amazonaws.services.ec2.model.UserBucketDetails;
import com.example.secondhandmarketwebapp.dao.CartDao;
import com.example.secondhandmarketwebapp.dao.OrderItemDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private UserService userService;
    @Autowired
    private CartDao cartDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public Cart getCart(String userEmail) {

        User user = userDao.getUserByEmail(userEmail);
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


    public void cleanCart() {
//        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//        String username = loggedInUser.getName();
//        User user = userService.getUser(username);
//        if (user != null) {
//            cartDao.removeAllCartItems(user.getCart());
//        }
        return;
    }




    public boolean stockSufficient() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);
        return cartDao.stockSufficient(orderItemDao.getItemsList(user.getCart().getId()));
    }

}
