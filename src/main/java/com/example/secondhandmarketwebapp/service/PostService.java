package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    public List<User> getUsers() {
        return postDao.getUsers();
    }
    public List<Post> getAllPostUnderOneUser(int userId) {
        return postDao.getAllPostUnderOneUser(userId);
    }
    public List<Post> getAllPost() {
        List<User> allUsers = postDao.getUsers();
        List<Post> allPosts = new ArrayList<>();
        for(User user : allUsers) {
            List<Post> userPosts = postDao.getAllPostUnderOneUser(user.getId());
            if(userPosts != null) {
                allPosts.addAll(userPosts);
            }
        }
        return allPosts;
    }
    public Post getPost (int postId) {
        return postDao.getPost(postId);
    }
    public void addPost(String userEmail, Post post) {
        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.addPost(userId,post);
    }
    public List<PostResponse> listAllProductsNearby(String zipcode, int distance) {
        List<Post> allPost = getAllPost();
        return postDao.listAllProductsNearby(allPost, zipcode,distance);
    }
}

