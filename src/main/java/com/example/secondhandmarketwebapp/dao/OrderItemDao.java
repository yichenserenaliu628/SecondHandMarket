package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderItemDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void save(OrderItem orderItem) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(orderItem);
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
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        boolean exists = false;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM OrderItem WHERE cart.id = :cartId AND post.id = :postId");
            query.setParameter("cartId", cartId);
            query.setParameter("postId", postId);
            OrderItem item = (OrderItem) query.getSingleResult();
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

    public void deleteOrderItem(int orderItemId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            OrderItem orderItem = session.get(OrderItem.class, orderItemId);
            Cart cart = orderItem.getCart();
            if (orderItem != null) {
                cart.setTotalPrice(cart.getTotalPrice() - orderItem.getQuantity() * orderItem.getPrice());
                cart.getOrderItemList().remove(orderItem); // remove the order item from the cart
                session.delete(orderItem);
                tx.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
