package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.OrderItemDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
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
        System.out.println(orderItem.getId()); // the id is 0?
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

    public boolean postExists(int postId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        User user = userService.getUserByEmail(username);
        System.out.println(user.getCart().getId());
        return orderItemDao.checkIfPostExist(user.getCart().getId(), postId);
    }


}
