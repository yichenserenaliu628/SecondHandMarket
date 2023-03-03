package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.ImageFormatException;
import com.example.secondhandmarketwebapp.exception.InvalidPostException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.MessageResponse;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.PostService;
import com.example.secondhandmarketwebapp.service.S3Service;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @Autowired
    private S3Service amazonClient;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUser() {
        return postService.getUsers();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PostResponse>> getAllPost() {
        try {
            List<PostResponse> allPosts = postService.getAllPostResponse();
            if (allPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allPosts, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/{email}/post", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PostResponse>> getPostsUnderSpecificUser(@PathVariable(value = "email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            List<PostResponse> allPostsUnderCurUser = postService.getAllPostUnderOneUser(user.getId());
            if (allPostsUnderCurUser.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allPostsUnderCurUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/post/{post_id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PostResponse> getPostsByPostId(@PathVariable(value = "post_id") int post_id) {
        try {
            PostResponse response = postService.getPostByPostId(post_id);
            if (response == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/allposts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PostResponse>> getAllPostsUnderCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByEmail(userDetails.getUsername());
            List<PostResponse> allPostsUnderCurUser = postService.getAllPostUnderOneUser(user.getId());
            if (allPostsUnderCurUser.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allPostsUnderCurUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/addPostOld", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post) {
        return postService.addPost(userDetails.getUsername(), post);
    }

    // new version of addPost method that enables uploading an image S3
    @PostMapping("/addPost")
    public ResponseEntity<?> createPostNew(@Valid @AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("zipcode") int zipcode,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Double price,
                                           @RequestParam("quantity") int quantity,
                                           @RequestParam("title") String title,
                                           @RequestParam("category") String category) {
        try {
            // create add post request
            AddProductRequest addProductRequest = new AddProductRequest();
            addProductRequest.setTitle(title);
            addProductRequest.setZipcode(zipcode);
            addProductRequest.setDescription(description);
            addProductRequest.setPrice(price);
            addProductRequest.setQuantity(quantity);
            addProductRequest.setCategory(category);
            // Upload image to S3
            String keyName = amazonClient.uploadFile(file);
            addProductRequest.setKeyName(keyName);

            postService.createPost(userDetails.getUsername(), addProductRequest);
            return ResponseEntity.ok(new MessageResponse("Product Added successfully!"));
        } catch (RuntimeException e) {
            logger.error("Failed to add new product " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        } catch (ImageFormatException e) {
            logger.error("Failed to add new product " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getErrorMessage());
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