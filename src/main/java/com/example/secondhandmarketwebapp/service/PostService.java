package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Post> getAllPost(int userId) {
        return postDao.getAllPost(userId);
    }
    public Post getPost (int postId) {
        return postDao.getPost(postId);
    }
    public void addPost(String userEmail, Post post) {
        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.addPost(userId,post);
    }
    public void deletePost(String userEmail, int id) {
//        require to interact with user or not?
        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.deletePost(userId, id);
    }
}

