package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.ProductImage;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.InvalidPostException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<User> getUsers() {
        return postDao.getUsers();
    }
    public List<Post> getAllPostUnderOneUser(int userId) {
        return postDao.getAllPostUnderOneUser(userId);
    }
    public List<Post> getAllPost() { return postDao.getPosts(); }

    public Post getPost (int postId) {
        return postDao.getPost(postId);
    }
    public ResponseEntity<String> addPost(String userEmail, Post post) throws InvalidPostException {
        if (!isValidZipCode(String.valueOf(post.getZipcode()))) {
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
        postDao.addPost(userId, post);
        return ResponseEntity.ok("Post added successfully.");
    }
    public List<PostResponse> listAllProductsNearby(String zipcode, int distance) {
        List<Post> allPost = getAllPost();
        return postDao.listAllProductsNearby(allPost, zipcode, distance);
    }

    public List<PostResponse> getProductByKeyword(String keyword) {
        List<Post> allPost = getAllPost();
        return postDao.getAllProductsByKeyword(allPost, keyword);
    }

    public List<PostResponse> sortProductByPriceLowToHigh() {
        List<Post> allPost = getAllPost();
        return postDao.sortProductByPriceLowToHigh(allPost);
    }

    public List<PostResponse> sortProductByPriceHighToLow() {
        List<Post> allPost = getAllPost();
        return postDao.sortProductByPriceHighToLow(allPost);
    }

    public List<PostResponse> filterProductByCategory(String category) {
        List<Post> allPost = getAllPost();
        return postDao.filterProductByCategory(allPost, category);
    }

    public List<PostResponse> filterProductByMaxPrice(Double max) {
        List<Post> allPost = getAllPost();
        return postDao.filterProductByMaxPrice(allPost, max);
    }
    public List<PostResponse> filterProductByPriceRange(Double min, Double max) {
        List<Post> allPost = getAllPost();
        return postDao.filterProductByPriceRange(allPost, min, max);
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
        List<Post> allPost = getAllPost();
        return postDao.filterProductBySellerRating(allPost, minRating);
    }

    @Transactional
    public ResponseEntity<String> createPost(String userEmail, AddProductRequest addProductRequest) throws InvalidPostException {
        if (!isValidZipCode(String.valueOf(addProductRequest.getZipcode()))) {
            throw new InvalidPostException("Invalid zipcode");
        }
        if (addProductRequest.getDescription() == null || addProductRequest.getDescription().isEmpty()) {
            throw new InvalidPostException("Description is required");
        }
        if (addProductRequest.getPrice() < 0) {
            throw new InvalidPostException("Price must be greater than or equal to zero");
        }
        if (addProductRequest.getQuantity() <= 0) {
            throw new InvalidPostException("Quantity must be greater than zero");
        }
        if (addProductRequest.getTitle() == null || addProductRequest.getTitle().isEmpty()) {
            throw new InvalidPostException("Title is required");
        }
        if (addProductRequest.getCategory() == null || addProductRequest.getCategory().isEmpty()) {
            throw new InvalidPostException("Category is required");
        }

        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.createPost(userId, addProductRequest);
        return ResponseEntity.ok("Post added successfully.");
    }
}