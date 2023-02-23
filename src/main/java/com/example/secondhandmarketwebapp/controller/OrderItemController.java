package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping(value = "/addtocart/{postId}/{quantity}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addItemToCart(@PathVariable("postId") int postId,
                              @PathVariable("quantity") int quantity) {
        orderItemService.saveOrderItem(postId, quantity);

    }

    @RequestMapping(value = "/cart/{orderItemId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteItemFromCart(@PathVariable("orderItemId") OrderItem orderItem) {
        orderItemService.deleteOrderItem(orderItem);
    }
}
