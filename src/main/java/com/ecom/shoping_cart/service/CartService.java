package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartService {
    Cart saveCart(Integer userId, Integer productId);
    List<Cart> getCartByUser(Integer userId);
    Integer getCartCount(Integer userId);

    void updateQuantity(Integer cardId, String type);
}
