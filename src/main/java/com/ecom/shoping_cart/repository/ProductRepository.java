package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findByIsActiveTrue();
}
