package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findByIsActiveTrue();
    Page<Product> findByIsActiveTrue(Pageable pageable);

    List<Product> findByIsActiveTrueAndCategory(String category);

    Page<Product> findByIsActiveTrueAndCategory(Pageable pageable, String category);

    List<Product> findByTitleContainingIgnoreCase(String keyword);

    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String keyword, String keyword1);
}
