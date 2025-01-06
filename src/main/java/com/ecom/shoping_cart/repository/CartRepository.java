package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserIdAndProductId(Integer userId, Integer productId);
}
