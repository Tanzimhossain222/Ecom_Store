package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserIdAndProductId(Integer userId, Integer productId);

    Integer countByUserId(Integer userId);

    List<Cart> findByUserId(Integer userId);


    void deleteByProduct(Product product);
}
