package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addItemCart(@PathVariable("postId") int postId) {
        orderItemService.saveOrderItem(postId);
    }

    @RequestMapping(value = "/cart/{orderItemId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteItemCart(@PathVariable("orderItemId") OrderItem orderItem) {
        orderItemService.deleteOrderItem(orderItem);
    }
}
