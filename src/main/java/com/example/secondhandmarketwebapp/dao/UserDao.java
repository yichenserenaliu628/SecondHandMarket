package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Authorities;
import com.example.secondhandmarketwebapp.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Repository
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void signUp(User user) {
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmail(user.getEmail());

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(user);
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

    public User getUser(String email) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
    public int getUserIdByEmail(String email) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT u.id FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        int userId = (int) query.getSingleResult();
        session.close();
        return userId;
    }
    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        User user = (User) query.getSingleResult();
        session.close();
        return user;
    }

    public User getUserByUserName(String username) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", username);
        User user = (User) query.getSingleResult();
        session.close();
        return user;
    }

    public boolean isValidUserName(String username) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("username", username));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count == 0;
    }

    public boolean isDupEmail(String email) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count == 0;
    }
}
