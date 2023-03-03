package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.service.OrderItemService;
import com.example.secondhandmarketwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private PostService postService;


    @RequestMapping(value = "/deletefromcart/{orderItemId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void deleteItemFromCart(@PathVariable("orderItemId") int orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
    }

    @RequestMapping(value = "/addtocart/{postId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addItemToCart(@PathVariable("postId") int postId) {
        if (!orderItemService.postExists(postId)) {
            orderItemService.saveOrderItem(postId);
        } else if (postService.isSoldOut(postId)){
            // TODO: report error message
            System.out.println("Product sold out");
        }
    }
}
