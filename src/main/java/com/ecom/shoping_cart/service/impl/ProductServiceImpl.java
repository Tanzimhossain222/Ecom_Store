package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.repository.ProductRepository;
import com.ecom.shoping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public boolean deleteProduct(Integer id) {

        Product product = productRepository.findById(id).orElse(null);
        System.out.println(product);
        if (product == null) {
            return false;
        }
        productRepository.delete(product);
        return true;

    }
}
