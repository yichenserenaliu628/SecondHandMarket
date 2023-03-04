package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.*;
import com.example.secondhandmarketwebapp.exception.CheckoutException;
import com.example.secondhandmarketwebapp.payload.request.AddProductRequest;
import com.example.secondhandmarketwebapp.payload.response.PostResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
import java.util.*;
import javax.json.JsonReader;

@Repository
public class PostDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CartDao cartDao;
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
        try  {
            List<Post> posts = getPosts();
            listOfPostResponses = generatePostResponses(posts);
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
                List<Post> postsUnderOneUser = user.getPostList();
                listOfPostsUnderOneUser = generatePostResponses(postsUnderOneUser);
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
            if (post != null) return generateOnePostResponse(post);
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
    public List<PostResponse> listAllProductsNearby(String zipcode, int distance) {
        List<PostResponse> listOfPostsNearby = new ArrayList<>();
        List<PostResponse> listOfAllPostResponses = getPostResponses();
        for (PostResponse post : listOfAllPostResponses) {
            if(calculateDistance(post.getZipcode(), zipcode) <= distance) {
                listOfPostsNearby.add(post);
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

    public List<PostResponse> searchProductByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        List<PostResponse> matchingPosts = new ArrayList<>();
        List<PostResponse> listOfAllPostResponses = getPostResponses();
        for (PostResponse post : listOfAllPostResponses) {
            if (post.getTitle().toLowerCase().contains(keyword)
                    || post.getDescription().toLowerCase().contains(keyword)
                    || post.getCategory().toLowerCase().contains(keyword)) {
                matchingPosts.add(post);
            }
        }
        return matchingPosts;

        /*Session session = sessionFactory.openSession();
        String hql = "FROM Post p WHERE p.title LIKE :searchKeyword " +
                "OR p.category LIKE :searchKeyword " +
                "OR p.description LIKE :searchKeyword";
        Query query = session.createQuery(hql);
        query.setParameter("searchKeyword", "%" + keyword + "%");
        List<Post> results = query.list();
        return generatePostResponses(results);*/
    }


    public List<PostResponse> sortProductByPriceLowToHigh() {
        /*List<PostResponse> listOfAllPostResponses = getPostResponses();
        List<PostResponse> results = new ArrayList<>(listOfAllPostResponses);
        Collections.sort(results, (o1, o2) -> {
            if (o1.getPrice() == o2.getPrice()) return 0;
            return o1.getPrice() < o2.getPrice() ? -1 : 1;
        });
        return results;*/
        Session session = sessionFactory.openSession();
        String hql = "FROM Post p ORDER BY p.price ASC";
        Query query = session.createQuery(hql);
        List<Post> results = query.list();
        return generatePostResponses(results);
    }

    public List<PostResponse> sortProductByPriceHighToLow() {
        /*List<PostResponse> listOfAllPostResponses = getPostResponses();
        List<PostResponse> results = new ArrayList<>(listOfAllPostResponses);
        Collections.sort(results, (o1, o2) -> {
            if (o1.getPrice() == o2.getPrice()) return 0;
            return o1.getPrice() < o2.getPrice() ? 1 : -1;
        });
        return results;*/
        Session session = sessionFactory.openSession();
        String hql = "FROM Post p ORDER BY p.price DESC";
        Query query = session.createQuery(hql);
        List<Post> results = query.list();
        return generatePostResponses(results);
    }

    public List<PostResponse> filterProductByCategory(String keyword) {
        Session session = sessionFactory.openSession();
        String keywordFirstLetterLowerCase = keyword.substring(0, 1).toLowerCase() + keyword.substring(1);
        String keywordFirstLetterUpperCase = keyword.substring(0, 1).toUpperCase() + keyword.substring(1);
        String hql = "FROM Post p WHERE p.category IN (:keywords)";
        Query query = session.createQuery(hql);
        query.setParameterList("keywords", new String[]{keywordFirstLetterLowerCase, keywordFirstLetterUpperCase});
        List<Post> results = query.list();
        return generatePostResponses(results);
    }


    public List<PostResponse> filterProductByMaxPrice(Double maxPrice) {
        Session session = sessionFactory.openSession();
        Query<Post> query = session.createQuery(
                "FROM Post WHERE price <= :maxPrice", Post.class);
        query.setParameter("maxPrice", maxPrice);
        List<Post> posts = query.list();
        return generatePostResponses(posts);
    }

    public List<PostResponse> filterProductByPriceRange(Double minPrice, Double maxPrice) {
        Session session = sessionFactory.openSession();
        Query<Post> query = session.createQuery(
                "FROM Post WHERE price BETWEEN :minPrice AND :maxPrice", Post.class);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        List<Post> posts = query.list();
        return generatePostResponses(posts);
    }

    public void deletePost(int userId, int postId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null && post.getUser().getId() == userId) {
                for (OrderItem orderItem : post.getOrderItem()) {
                    cartDao.removeCartItem(orderItem.getId());
                    OrderItem orderItemOriginalEntry = session.get(OrderItem.class, orderItem.getId());
                    session.delete(orderItemOriginalEntry);
                }
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

    public List<PostResponse> filterProductBySellerRating(Double minRating) {
        List<PostResponse> listOfPostResponsesBySellerRating = new ArrayList<>();
        List<PostResponse> listOfAllPostResponses = getPostResponses();
        for (PostResponse post : listOfAllPostResponses) {
            if (post.getSellerRating() >= minRating) {
                listOfPostResponsesBySellerRating.add(post);
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

    public String generateImageUrl(Post post) {
        return post.getKeyName() != null ? endpointUrl + "/" + bucketName + "/" + post.getKeyName() : null;
    }

    public List<PostResponse> generatePostResponses (List<Post> posts) {
        List<PostResponse> results = new ArrayList<>();
        for (Post post : posts) {
            results.add(generateOnePostResponse(post));
        }
        return results;
    }

    public PostResponse generateOnePostResponse (Post post) {
        double rating = findAverageRating(post);
        String imageUrl = generateImageUrl(post);
        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .description(post.getDescription())
                .zipcode(post.getZipcode())
                .quantity(post.getQuantity())
                .category(post.getCategory())
                .isSold(post.isSold())
                .sellerEmail(post.getUser().getEmail())
                .sellerRating(rating)
                .imageUrl(imageUrl)
                .build();
        return response;
    }

}