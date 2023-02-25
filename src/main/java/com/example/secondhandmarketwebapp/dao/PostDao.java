package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Post;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        for (Post post : allPost) {
            if(String.valueOf(post.getZipcode()).equals("60618") /*&& calculateDistance(String.valueOf(post.getZipcode()), zipcode) <= distance*/) {
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

    public List<PostResponse> getAllProductsByKeyword(List<Post> allPost, String keyword) {
        List<PostResponse> listOfPostResponsesContainingKeyword = new ArrayList<>();
        List<Post> listOfPostsContainingKeyword = findByCategoryContaining(allPost, keyword);

        for (Post post : listOfPostsContainingKeyword) {
                PostResponse response = PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .description(post.getDescription())
                        .zipcode(String.valueOf(post.getZipcode())).build();
            listOfPostResponsesContainingKeyword.add(response);
        }
        return listOfPostResponsesContainingKeyword;
    }

    public List<Post> findByCategoryContaining(List<Post> allPost, String keyword) {
        List<Post> listOfPostsCategoryContainingKeyword = new ArrayList<>();
        for (Post post : allPost) {
            if (post.getCategory().contains(keyword)) {
                listOfPostsCategoryContainingKeyword.add(post);
            }
        }
        return listOfPostsCategoryContainingKeyword;
    }

    public List<PostResponse> sortProductByPriceLowToHigh(List<Post> allPost) {
        List<PostResponse> sortedProductsByPrice = new ArrayList<>();
        Collections.sort(allPost, Comparator.comparingDouble(Post::getPrice));

        for (Post post : allPost) {
            PostResponse response = PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode())).build();
            sortedProductsByPrice.add(response);
        }
        return sortedProductsByPrice;
    }

    public List<PostResponse> sortProductByPriceHighToLow(List<Post> allPost) {
        List<PostResponse> sortedProductsByPriceHighToLow = new ArrayList<>();
        Collections.sort(allPost, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                if (o1.getPrice() == o2.getPrice()) return 0;
                return o1.getPrice() < o2.getPrice() ? 1 : -1;
            }
        });
        for (Post post : allPost) {
            PostResponse response = PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode())).build();
            sortedProductsByPriceHighToLow.add(response);
        }
        return sortedProductsByPriceHighToLow;
    }

    public List<PostResponse> filterProductByCategory(List<Post> allPost, String category) {
        List<PostResponse> listOfFilteredProductByCategory = new ArrayList<>();
        List<Post> filteredProductByCategory = filterByCategory(allPost, category);

        for (Post post : filteredProductByCategory) {
            PostResponse response = PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode())).build();
            listOfFilteredProductByCategory.add(response);
        }
        return listOfFilteredProductByCategory;
    }

    public List<Post> filterByCategory(List<Post> allPost, String category) {
        List<Post> filteredProductByCategory = new ArrayList<>();
        for (Post post : allPost) {
            if (post.getCategory().equals(category)) {
                filteredProductByCategory.add(post);
            }
        }
        return filteredProductByCategory;
    }

    public List<PostResponse> filterProductByMaxPrice(List<Post> allPost, Double max) {
        List<PostResponse> listOfFilteredProductByMaxPrice = new ArrayList<>();
        List<Post> filteredProductByMaxPrice = filterByMaxPrice(allPost, max);

        for (Post post : filteredProductByMaxPrice) {
            PostResponse response = PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode())).build();
            listOfFilteredProductByMaxPrice.add(response);
        }
        return listOfFilteredProductByMaxPrice;
    }

    public List<Post> filterByMaxPrice(List<Post> allPost, Double max) {
        List<Post> filteredProductByMaxPrice = new ArrayList<>();
        for (Post post : allPost) {
            if (post.getPrice() <= max) {
                filteredProductByMaxPrice.add(post);
            }
        }
        return filteredProductByMaxPrice;
    }
    // filterProductByPriceRange
    public List<PostResponse> filterProductByPriceRange(List<Post> allPost, Double min, Double max) {
        List<PostResponse> listOfFilteredProductByPriceRange = new ArrayList<>();
        List<Post> filteredProductByMaxPrice = filterByPriceRange(allPost, min, max);

        for (Post post : filteredProductByMaxPrice) {
            PostResponse response = PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode())).build();
            listOfFilteredProductByPriceRange.add(response);
        }
        return listOfFilteredProductByPriceRange;
    }

    public List<Post> filterByPriceRange(List<Post> allPost, Double min, Double max) {
        List<Post> filteredProductByPriceRange = new ArrayList<>();
        for (Post post : allPost) {
            if (post.getPrice() <= max && post.getPrice() >= min) {
                filteredProductByPriceRange.add(post);
            }
        }
        return filteredProductByPriceRange;
    }
}