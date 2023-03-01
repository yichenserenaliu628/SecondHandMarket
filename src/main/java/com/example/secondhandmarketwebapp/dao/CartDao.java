package com.example.secondhandmarketwebapp.dao;

import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.OrderItem;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice() * cartItem.getQuantity());
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

    public Set<String> removeAllCartItems(Cart cart) {
        Set<String> sellerSets = new HashSet<>();
        for (OrderItem item : cart.getOrderItemList()) {
            sellerSets.add(item.getPost().getUser().getUsername());
            postDao.updatePostQuantity(item.getPost(), item.getQuantity());
            removeCartItem(item.getId());
        }
        return sellerSets;
    }

    public boolean stockSufficient(List<OrderItem> itemList) {
        for (OrderItem item : itemList) {
            if (item.getQuantity() > item.getPost().getQuantity()) {
                return false;
            }
        }
        return true;
    }
}


