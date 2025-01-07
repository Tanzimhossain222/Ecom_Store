package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(Integer userId);
}
