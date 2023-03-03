package com.example.secondhandmarketwebapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.secondhandmarketwebapp.config.CacheConfig;
import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.ProductImage;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.InvalidPostException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;
    @Autowired
    private S3Service amazonClient;

    public List<User> getUsers() {
        return postDao.getUsers();
    }


    //@Cacheable(value = CacheConfig.POSTS_CACHE)
    public List<Post> getAllPostResponse() { return postDao.getPosts(); }
    public Post getPost (int postId) {
        return postDao.getPost(postId);
    }
    public Post getPostByPostId (int postId) {
        return postDao.getPostById(postId);
    }

    public List<PostResponse> listAllProductsNearby(String zipcode, int distance) {
        return postDao.listAllProductsNearby(zipcode, distance);
    }

    public List<PostResponse> searchProductByKeyword(String keyword) {
        return postDao.searchProductByKeyword(keyword);
    }

    public List<PostResponse> sortProductByPriceLowToHigh() {
        return postDao.sortProductByPriceLowToHigh();
    }

    public List<PostResponse> sortProductByPriceHighToLow() {
        return postDao.sortProductByPriceHighToLow();
    }

    public List<PostResponse> filterProductByCategory(String category) {
        return postDao.filterProductByCategory(category);
    }

    public List<PostResponse> filterProductByMaxPrice(Double max) {
        return postDao.filterProductByMaxPrice(max);
    }
    public List<PostResponse> filterProductByPriceRange(Double min, Double max) {
        return postDao.filterProductByPriceRange(min, max);
    }

    public void deletePost(String userEmail, int id) {
        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.deletePost(userId, id);
    }

    public boolean isSoldOut(int postId) {
        return postDao.isSoldOut(postId);
    }

    public boolean isValidZipCode(String zipCode) {
        try {
            if(zipCode.length() != 5) {
                return false;
            }
            String url = "https://www.mapquestapi.com/geocoding/v1/address?key=2EsrDBJE7aeBUgday06d7Grj84ccUvfY&postalCode=" + zipCode;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            return response.contains("\"postalCode\":\"" + zipCode + "\",\"geocodeQualityCode\"");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PostResponse> filterProductBySellerRating(Double minRating) {
        return postDao.filterProductBySellerRating(minRating);
    }

    @Transactional
    public ResponseEntity<String> createPost(String userEmail, Post post) throws InvalidPostException {
        if (!isValidZipCode((post.getZipcode()))) {
            throw new InvalidPostException("Invalid zipcode");
        }
        if (post.getDescription() == null || post.getDescription().isEmpty()) {
            throw new InvalidPostException("Description is required");
        }
        if (post.getPrice() < 0) {
            throw new InvalidPostException("Price must be greater than or equal to zero");
        }
        if (post.getQuantity() <= 0) {
            throw new InvalidPostException("Quantity must be greater than zero");
        }
        if (post.getTitle() == null || post.getTitle().isEmpty()) {
            throw new InvalidPostException("Title is required");
        }
        if (post.getCategory() == null || post.getCategory().isEmpty()) {
            throw new InvalidPostException("Category is required");
        }

        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.createPost(userId, post);
        return ResponseEntity.ok("Post added successfully.");
    }

}