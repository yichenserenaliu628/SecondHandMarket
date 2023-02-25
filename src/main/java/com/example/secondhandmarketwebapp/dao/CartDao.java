package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CartDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PostDao postDao;

    public void removeCartItem(int itemId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            OrderItem cartItem = session.get(OrderItem.class, itemId);
            Cart cart = cartItem.getCart();
            cart.getOrderItemList().remove(cartItem);

            session.beginTransaction();
            session.delete(cartItem);
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void removeAllCartItems(List<OrderItem> itemList) {
        for (OrderItem item : itemList) {
            removeCartItem(item.getId());
        }
    }

    public boolean stockSufficient(List<OrderItem> itemList) {
        System.out.println("stockSufficient starts");
        for (OrderItem item : itemList) {
            if (item.getQuantity() > postDao.getPostQuantity(item.getPost().getId())) {
                return false;
            }
        }
        removeAllCartItems(itemList);
        return true;
    }
}


