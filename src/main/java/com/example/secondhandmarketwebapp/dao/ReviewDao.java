package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Review;
import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.payload.request.ReviewSellerRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDao {

    @Autowired
    private SessionFactory sessionFactory;
    public void addReview(User seller, ReviewSellerRequest reviewSellerRequest) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            if (reviewSellerRequest != null) {
                Review review = new Review();
                review.setUser(seller);
                review.setRating(reviewSellerRequest.getRating());
                review.setComment(reviewSellerRequest.getComment());
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}