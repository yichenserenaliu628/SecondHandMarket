package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUser() {
        return postService.getUsers();
    }

    @RequestMapping(value = "/user/{userId}/post", method = RequestMethod.GET)
    @ResponseBody
    public List<Post> getPosts(@PathVariable(value = "userId") int userId) {
        return postService.getAllPost(userId);
    }

    @RequestMapping(value = "/user/{userId}/post", method = RequestMethod.POST)
    @ResponseBody
    public List<Post> createPost(@PathVariable(value = "userId") int userId) {
        return postService.getAllPost(userId);
    }
}
