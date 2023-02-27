package com.example.secondhandmarketwebapp.service;

import com.example.secondhandmarketwebapp.dao.PostDao;
import com.example.secondhandmarketwebapp.dao.UserDao;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
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
    public void addPost(String userEmail, Post post) {
        int userId = userDao.getUserIdByEmail(userEmail);
        postDao.addPost(userId, post);
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
}