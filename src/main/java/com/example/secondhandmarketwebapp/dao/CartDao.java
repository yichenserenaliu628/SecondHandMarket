package com.example.secondhandmarketwebapp.dao;


import com.example.secondhandmarketwebapp.entity.Cart;
import com.example.secondhandmarketwebapp.entity.ListedItems;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class CartDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void removeCartItem(int itemId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            ListedItems cartItem = session.get(ListedItems.class, itemId);
            /*Cart cart = cartItem.getCart();
            cart.getOrderItemList().remove(cartItem);

            session.beginTransaction();
            session.delete(cartItem);
            session.getTransaction().commit();*/

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

    public void removeAllCartItems(Cart cart) {
/*        for (ListedItems item : cart.getListItem()) {
            removeCartItem(item.getId());
        }*/
    }
}


