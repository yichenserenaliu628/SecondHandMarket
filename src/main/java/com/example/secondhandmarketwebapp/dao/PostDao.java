package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Post> getAllPost(int userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
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

    public void deletePost(int userId, int postId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null && post.getUser().getId() == userId) {
                session.delete(post);
                tx.commit();
            }
        } catch (Exception ex) {
        ex.printStackTrace();
        }
    }

    public Post getPostById(int postId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM Post WHERE id = :postId");
        query.setParameter("postId", postId);
        Post post = (Post) query.getSingleResult();
        session.close();
        return post;
    }

    public int getPostQuantity(int postId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT p.quantity FROM Post p WHERE p.id = :postId");
        query.setParameter("postId", postId);
        int quantity = (int) query.getSingleResult();
        session.close();
        return quantity;
    }


}
