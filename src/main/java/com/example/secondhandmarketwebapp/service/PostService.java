package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PostService {
    @Autowired
    private PostDao postDao;


    public List<User> getUsers() {
        return postDao.getUsers();
    }

    public List<Post> getAllPost(int userId) {
        return postDao.getAllPost(userId);
    }

    public Post getPost (int postId) {
        return postDao.getPost(postId);
    }
}

