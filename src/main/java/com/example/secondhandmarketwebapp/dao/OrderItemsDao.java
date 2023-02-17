package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemsDao {
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

}
