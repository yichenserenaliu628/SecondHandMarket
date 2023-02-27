package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        return postService.getAllPostUnderOneUser(userId);
    }

    @RequestMapping(value = "/user/{userId}/post", method = RequestMethod.POST)
    @ResponseBody
    public List<Post> createPost(@PathVariable(value = "userId") int userId) {
        return postService.getAllPostUnderOneUser(userId);
    }
    @RequestMapping(value = "/addPost", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post) {
        if (!postService.isValidZipCode((String.valueOf(post.getZipcode())))) {
            return ResponseEntity.badRequest().body("Invalid zipcode. Please provide a valid zipcode.");
        }
        postService.addPost(userDetails.getUsername(), post);
        return ResponseEntity.ok("Post added successfully.");
    }
    @DeleteMapping("/deletePost/{id}")
    @ResponseBody
    public void deletePostById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int id) {
        postService.deletePost(userDetails.getUsername(), id);
    }
}