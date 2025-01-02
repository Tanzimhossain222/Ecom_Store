package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product saveProduct(Product product);

    List<Product> getAllProduct();

    boolean deleteProduct(Integer id);
}
