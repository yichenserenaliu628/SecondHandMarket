package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDao {
    @Autowired
    private SessionFactory sessionFactory;
    public void addReview(Review review) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            if (review != null) {
                review.getUser().getReviewList().add(review);
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
