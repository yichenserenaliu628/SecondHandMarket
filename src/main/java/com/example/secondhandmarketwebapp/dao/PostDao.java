package com.example.secondhandmarketwebapp.dao;

import com.amazonaws.services.securityhub.model.Product;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.ProductImage;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.json.JsonReader;

@Repository
public class PostDao {
    @Autowired
    private SessionFactory sessionFactory;
    // https://www.baeldung.com/hibernate-criteria-queries
    public List<User> getUsers() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            return session.createQuery(criteria).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Post> getAllPostUnderOneUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                user.getPostList().size();
                return user.getPostList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Post getPost(int postId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Post.class, postId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public void addPost(int userId, Post post) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                user.getPostList().size();
                user.getPostList().add(post);
                post.setUser(user);
                session.save(post);
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<PostResponse> listAllProductsNearby(List<Post> allPost, String zipcode, int distance) {
        List<PostResponse> listOfPostsNearby = new ArrayList<>();
        for  (Post post : allPost) {
            if(!String.valueOf(post.getZipcode()).equals("95089") && calculateDistance(String.valueOf(post.getZipcode()), zipcode) <= distance) {
                PostResponse response = PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .description(post.getDescription())
                        .zipcode(String.valueOf(post.getZipcode())).build();
                listOfPostsNearby.add(response);
            }
        }
        return listOfPostsNearby;
    }
    public static double calculateDistance(String zip1, String zip2) {
        final String API_KEY = "2EsrDBJE7aeBUgday06d7Grj84ccUvfY";
        String url = "http://www.mapquestapi.com/directions/v2/route?key=" + API_KEY + "&from=" + URLEncoder.encode(zip1) + "&to=" + URLEncoder.encode(zip2);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            JsonReader reader = Json.createReader(responseStream);
            JsonObject jsonResponse = reader.readObject();
            double distance = jsonResponse.getJsonObject("route").getJsonNumber("distance").doubleValue();
            reader.close();
            return distance;
        } catch (IOException e) {
            System.err.println("Error calculating distance: " + e.getMessage());
            return -1;
        }
    }


}
