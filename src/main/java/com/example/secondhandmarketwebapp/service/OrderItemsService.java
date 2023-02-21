package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.OrderItemsDao;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsService {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemsDao orderItemsDao;

    public void saveOrderItem(int postId) {
        OrderItem orderItem = new OrderItem();
        Post post = postService.getPost(postId);

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);

        orderItem.setPost(post);
        orderItem.setCart(user.getCart());
        orderItem.setQuantity(1);
        orderItem.setPrice(post.getPrice());
        orderItemsDao.save(orderItem);
    }
}
