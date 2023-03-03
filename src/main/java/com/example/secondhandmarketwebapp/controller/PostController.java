package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.config.CacheConfig;
import com.example.secondhandmarketwebapp.dao.PostDao;
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
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = CacheConfig.POSTS_CACHE)
    @ResponseBody
    public ResponseEntity<List<Post>> getAllPost() {
        try {
            List<Post> allPosts = postService.getAllPostResponse();
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
    public ResponseEntity<User> getPostsUnderSpecificUser(@PathVariable(value = "email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   /* @RequestMapping(value = "/user/post/{post_id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Post> getPostsByPostId(@PathVariable(value = "post_id") int post_id) {
        try {
            Post response = postService.getPostByPostId(post_id);
            if (response == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

/*    @RequestMapping(value = "/user/allposts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<PostResponse>> getAllPostsUnderCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.getUserByUserName(userDetails.getUsername());
            List<PostResponse> allPostsUnderCurUser = postService.getAllPostUnderOneUser(user.getId());
            if (allPostsUnderCurUser.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allPostsUnderCurUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

/*    @GetMapping("/averageRating")
    @ResponseBody
    public ResponseEntity<Double> getRating(@PathVariable(value = "user") User user) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // new version of addPost method that enables uploading an image to S3
    @PostMapping("/addPost")
    public ResponseEntity<?> createPostNew(@Valid @AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("zipcode") String zipcode,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Double price,
                                           @RequestParam("quantity") int quantity,
                                           @RequestParam("title") String title,
                                           @RequestParam("category") String category) {
        try {
            //todo why we need addProductRequest
            // create add post request
            Post post = new Post();
            post.setTitle(title);
            post.setZipcode(zipcode);
            post.setDescription(description);
            post.setPrice(price);
            post.setQuantity(quantity);
            post.setCategory(category);
            // Upload image to S3
            String keyName = amazonClient.uploadFile(file);
            post.setKeyName(keyName);
            postService.createPost(userDetails.getUsername(), post);
            return ResponseEntity.ok(new MessageResponse("Product Added successfully!"));
        } catch (InvalidPostException e) {
            logger.error("Failed to add new product " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getErrorMessage());
        } catch (ImageFormatException e) {
            logger.error("Image format invalid " + e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getErrorMessage());
        }
    }

    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<String> handleInvalidPostException(InvalidPostException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorMessage());
    }

    @DeleteMapping("/deletePost/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePostById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int id) {
        postService.deletePost(userDetails.getUsername(), id);
        return ResponseEntity.ok(new MessageResponse("Product delete successfully!"));
    }
}