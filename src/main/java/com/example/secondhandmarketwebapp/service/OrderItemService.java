package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.OrderItemDao;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderItemDao orderItemDao;
    public void saveOrderItem(int postId, int quantity) {
        OrderItem orderItem = new OrderItem();
        Post post = postService.getPost(postId);

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);

        orderItem.setPost(post);
        orderItem.setCart(user.getCart());
        orderItem.setQuantity(quantity);
        orderItem.setPrice(post.getPrice());
        orderItemDao.save(orderItem);
    }

    public void deleteOrderItem(OrderItem orderItem) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);

        orderItem.setCart(user.getCart());
        orderItem.setQuantity(0);
        orderItemDao.delete(orderItem);
    }
}
