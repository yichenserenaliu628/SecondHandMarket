package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;


// To this import statement:
import java.util.List;


@Repository
public class OrderItemDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(OrderItem orderItem) {
        Session session = null;
        try {
            // Open the new session
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Save orderItem
            session.save(orderItem);

            // Update the total price in the cart
            Cart cart = orderItem.getCart();
            cart.setTotalPrice(cart.getTotalPrice() + orderItem.getPrice());
            session.update(cart);

            // Commit session change
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public void delete(OrderItem orderItem) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(orderItem);
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean checkIfPostExist(int cartId, int postId) {
        System.out.println("check starts");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        boolean exists = false;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM OrderItem WHERE cart.id = :cartId AND post.id = :postId");
            query.setParameter("cartId", cartId);
            query.setParameter("postId", postId);
            System.out.println("--------------------------------------");
            OrderItem item = (OrderItem) query.getSingleResult();
            System.out.println(item.getPost().getId());
            if (item != null) {
                exists = true;
                item.setQuantity(item.getQuantity() + 1);
                session.update(item);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exists;
    }

    public List<OrderItem> getItemsList(int cartId) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM OrderItem WHERE cart.id = :cartId");
        query.setParameter("cartId", cartId);
        List<OrderItem> orderItems = query.getResultList();
        session.close();
        return orderItems;
    }


}




