package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.service.PostService;
import com.example.secondhandmarketwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUser() {
        return postService.getUsers();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    @ResponseBody
    public List<Post> getPost() {
        return postService.getAllPost();
    }

    @RequestMapping(value = "/user/{email}/post", method = RequestMethod.GET)
    @ResponseBody
    public List<Post> getPosts(@PathVariable(value = "email") String email) {
        User user = userService.getUserByEmail(email);
        return postService.getAllPostUnderOneUser(user.getId());
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