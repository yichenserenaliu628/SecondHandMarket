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
    public void addReview(User seller,  double rating, String comment) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Review review = new Review();
            review.setUser(seller);
            review.setRating(rating);
            review.setComment(comment);
            session.save(review);
            updateRating(seller);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateRating(User seller) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            List<Review> lists = seller.getReviewList();
            double rating = 0;
            for(Review list : lists){
                rating += list.getRating();
            }
            seller.setAverageRating(rating/lists.size());
            session.save(seller);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}