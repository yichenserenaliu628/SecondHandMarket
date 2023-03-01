package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.InvalidPostException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.MessageResponse;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.PostService;
import com.example.secondhandmarketwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
@Controller
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
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
        return postService.addPost(userDetails.getUsername(), post);
    }

    // new addPost method that enables uploading an image S3
    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@Valid @AuthenticationPrincipal UserDetails userDetails, @RequestBody AddProductRequest addProductRequest) {
        try {
            postService.createPost(userDetails.getUsername(), addProductRequest);
            return ResponseEntity.ok(new MessageResponse("Product Added successfully!"));
        } catch (RuntimeException e) {
            logger.error("Failed to add new product " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<String> handleInvalidPostException(InvalidPostException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorMessage());
    }

    @DeleteMapping("/deletePost/{id}")
    @ResponseBody
    public void deletePostById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int id) {
        postService.deletePost(userDetails.getUsername(), id);
    }
}