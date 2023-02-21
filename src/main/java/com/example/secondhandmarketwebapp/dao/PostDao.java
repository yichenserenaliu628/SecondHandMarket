package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.OrderItem;
import com.example.secondhandmarketwebapp.entity.Post;
import com.example.secondhandmarketwebapp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
