package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.ProductImage;
import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.CheckoutException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import com.example.secondhandmarketwebapp.service.S3Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Query;
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
    @Autowired
    private S3Service amazonClient;
    private String endpointUrl = "https://s3.us-west-1.amazonaws.com";
    private String bucketName = "serenaliuawsbucket";
    private String accessKey = "AKIARHORSLJOXTQCKYRM";
    private String secretKey = "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";

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
    public List<Post> getPosts() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
            criteria.from(Post.class);
            return session.createQuery(criteria).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<PostResponse> getPostResponses() {
        List<PostResponse> listOfPostResponses = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
            criteria.from(Post.class);
            List<Post> posts = session.createQuery(criteria).getResultList();
            for(Post post : posts) {
                double rating = findAverageRating(post);
                // download post image from s3
                String keyName = post.getKeyName();
                String imageUrl = "";
                if (keyName != null) {
                    imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
                }
                PostResponse response = PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .description(post.getDescription())
                        .zipcode(String.valueOf(post.getZipcode()))
                        .quantity(post.getQuantity())
                        .category(post.getCategory())
                        .isSold(post.isSold())
                        .sellerEmail(post.getUser().getEmail())
                        .sellerRating(rating)
                        .imageUrl(imageUrl)
                        .build();
                listOfPostResponses.add(response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfPostResponses;
    }
    public List<PostResponse> getAllPostUnderOneUser(int userId) {
        List<PostResponse> listOfPostsUnderOneUser = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                user.getPostList().size();
                for(Post post : user.getPostList()) {
                    double rating = findAverageRating(post);
                    // download post image from s3
                    String keyName = post.getKeyName();
                    String imageUrl = "";
                    if (keyName != null) {
                        imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
                    }
                    // generate post repsonse
                    PostResponse response = PostResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .description(post.getDescription())
                            .zipcode(String.valueOf(post.getZipcode()))
                            .quantity(post.getQuantity())
                            .category(post.getCategory())
                            .isSold(post.isSold())
                            .sellerEmail(post.getUser().getEmail())
                            .sellerRating(rating)
                            .imageUrl(imageUrl)
                            .build();
                    listOfPostsUnderOneUser.add(response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfPostsUnderOneUser;
    }
    public Post getPost(int postId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Post.class, postId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public PostResponse getPostByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);
            if(post == null) {
                return null;
            }
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
            return response;
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
            if(calculateDistance(String.valueOf(post.getZipcode()), zipcode) <= distance) {
                double rating = findAverageRating(post);
                // download post image from s3
                String keyName = post.getKeyName();
                String imageUrl = "";
                if (keyName != null) {
                    imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
                }
                PostResponse response = PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .description(post.getDescription())
                        .zipcode(String.valueOf(post.getZipcode()))
                        .quantity(post.getQuantity())
                        .category(post.getCategory())
                        .isSold(post.isSold())
                        .sellerEmail(post.getUser().getEmail())
                        .sellerRating(rating)
                        .imageUrl(imageUrl)
                        .build();
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

    public double findAverageRating(Post post) {
        User user = post.getUser();
        int user_id = user.getId();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT r FROM Review r WHERE r.user.id = :user_id");
        query.setParameter("user_id", user_id);
        List<Review> userReviewList = query.getResultList();
        if(userReviewList == null || userReviewList.size() == 0) {
            //this user does not have any ratings yet!
            return 0;
        }
        double averageRating = 0;
        for(Review review : userReviewList) {
            if(review != null && review.getRating() >= 0) {
                averageRating += review.getRating();
            }
        }
        return averageRating / userReviewList.size();
    }

    public List<PostResponse> getAllProductsByKeyword(List<Post> allPost, String keyword) {
        List<PostResponse> listOfPostResponsesContainingKeyword = new ArrayList<>();
        List<Post> listOfPostsContainingKeyword = findByCategoryContaining(allPost, keyword);

        for (Post post : listOfPostsContainingKeyword) {
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
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
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
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
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
            sortedProductsByPriceHighToLow.add(response);
        }
        return sortedProductsByPriceHighToLow;
    }

    public List<PostResponse> filterProductByCategory(List<Post> allPost, String category) {
        List<PostResponse> listOfFilteredProductByCategory = new ArrayList<>();
        List<Post> filteredProductByCategory = filterByCategory(allPost, category);

        for (Post post : filteredProductByCategory) {
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
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
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
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
    public List<PostResponse> filterProductByPriceRange(List<Post> allPost, Double min, Double max) {
        List<PostResponse> listOfFilteredProductByPriceRange = new ArrayList<>();
        List<Post> filteredProductByMaxPrice = filterByPriceRange(allPost, min, max);

        for (Post post : filteredProductByMaxPrice) {
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            PostResponse response = PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .zipcode(String.valueOf(post.getZipcode()))
                    .quantity(post.getQuantity())
                    .category(post.getCategory())
                    .isSold(post.isSold())
                    .sellerEmail(post.getUser().getEmail())
                    .sellerRating(rating)
                    .imageUrl(imageUrl)
                    .build();
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


    public void deletePost(int userId, int postId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null && post.getUser().getId() == userId) {
                String keyName = post.getKeyName();
                amazonClient.deleteFile(keyName);
                session.delete(post);
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePostQuantity(Post post, int quantity) throws CheckoutException  {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            int oldQuantity = post.getQuantity();
            if (oldQuantity <= 0) {
                post.setSold(true);
                throw new CheckoutException("Sorry. The product is sold out.");
            }

            if (oldQuantity < quantity) {
                throw new CheckoutException("Sorry. The seller does not have sufficient stock of the item. Please reduce quantity.");
            }

            int newQuantity = oldQuantity - quantity;
            post.setQuantity(newQuantity);
            if (newQuantity == 0) {
                post.setSold(true);
            }

            session.update(post);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isSoldOut(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);
            if (post != null) {
                return post.isSold();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Post getPostById(int postId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM Post WHERE id = :postId");
        query.setParameter("postId", postId);
        Post post = (Post) query.getSingleResult();
        session.close();
        return post;
    }

    public List<PostResponse> filterProductBySellerRating(List<Post> allPost, Double minRating) {
        List<PostResponse> listOfPostResponsesBySellerRating = new ArrayList<>();

        for (Post post : allPost) {
            double rating = findAverageRating(post);
            // download post image from s3
            String keyName = post.getKeyName();
            String imageUrl = "";
            if (keyName != null) {
                imageUrl = endpointUrl + "/" + bucketName + "/" + keyName;
            }
            if (rating >= minRating) {
                PostResponse response = PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .price(post.getPrice())
                        .description(post.getDescription())
                        .zipcode(String.valueOf(post.getZipcode()))
                        .quantity(post.getQuantity())
                        .category(post.getCategory())
                        .isSold(post.isSold())
                        .sellerEmail(post.getUser().getEmail())
                        .sellerRating(rating)
                        .imageUrl(imageUrl)
                        .build();
                listOfPostResponsesBySellerRating.add(response);
            }
        }
        return listOfPostResponsesBySellerRating;
    }

    public void createPost(int userId, AddProductRequest addProductRequest) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {

                user.getPostList().size();
                Post newPost = Post.builder()
                        .zipcode(addProductRequest.getZipcode())
                        .description(addProductRequest.getDescription())
                        .price(addProductRequest.getPrice())
                        .quantity(addProductRequest.getQuantity())
                        .title(addProductRequest.getTitle())
                        .isSold(false)
                        .user(user)
                        .category(addProductRequest.getCategory())
                        .keyName(addProductRequest.getKeyName()).build();

                user.getPostList().add(newPost);
                session.save(newPost);
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}