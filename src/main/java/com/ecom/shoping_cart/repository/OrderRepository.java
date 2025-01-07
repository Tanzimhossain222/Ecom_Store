package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Integer> {
}
