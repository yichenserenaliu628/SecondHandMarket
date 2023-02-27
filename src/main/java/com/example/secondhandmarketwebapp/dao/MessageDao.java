package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Message;
import com.example.secondhandmarketwebapp.entity.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MessageDao {

    @Autowired
    private SessionFactory sessionFactory;
    public String findBySenderId(int senderId) {
        try (Session session = sessionFactory.openSession()) {
            Message sender = session.get(Message.class, senderId);
            if (sender != null) {
                return sender.getMessage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public String findByReceiverId(int receiverId) {
        try (Session session = sessionFactory.openSession()) {
            Message receiver = session.get(Message.class, receiverId);
            if (receiver != null) {
                return receiver.getMessage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(Message message) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(message);
            tx.commit();
        } catch (Exception ex) {
        ex.printStackTrace();
        }
    }
}
