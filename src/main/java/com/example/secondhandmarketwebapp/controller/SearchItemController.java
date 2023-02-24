package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SearchItemController {
//    private static final Logger logger = LoggerFactory.getLogger(SearchItemController.class);
    @Autowired
    private PostService postService;

    @GetMapping("/searchItem/listNearby/{zipcode},{distance}")
    public ResponseEntity<List<PostResponse>> listAllProductsNearby(@PathVariable String zipcode, @PathVariable int distance) {
        try {
            List<PostResponse> allNearbyPosts = postService.listAllProductsNearby(zipcode, distance);
            if (allNearbyPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allNearbyPosts, HttpStatus.OK);
        } catch (RuntimeException e) {
//            logger.error("Failed to list all posts nearby zipcode :" + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
