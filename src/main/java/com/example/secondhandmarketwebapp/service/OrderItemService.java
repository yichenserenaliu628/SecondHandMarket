package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.OrderItemDao;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.hibernate.criterion.Order;
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
    public void saveOrderItem(int postId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        final OrderItem orderItem = new OrderItem();
        final Post post = postService.getPost(postId);
        orderItem.setPost(post);
        orderItem.setCart(user.getCart()); // correct

        orderItem.setQuantity(1);
        orderItem.setPrice(post.getPrice());
        orderItemDao.save(orderItem);
    }

    public void deleteOrderItem(int orderItemId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        for (OrderItem item : user.getCart().getOrderItemList()) {
            if (item.getId() == orderItemId) {
                orderItemDao.updateOrderItem(orderItemId);
                return;
            }
        }
    }

    public boolean postExists(int postId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        return orderItemDao.checkIfPostExist(user.getCart().getId(), postId);
    }
}
