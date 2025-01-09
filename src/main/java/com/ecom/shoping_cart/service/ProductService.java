package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {
    Product saveProduct(Product product);

    List<Product> getAllProduct();
    List<Product> getAllActiveProduct(String category);

    boolean deleteProduct(Integer id);

    Product getProductById(Integer id);
    Product updateProduct(Product product, MultipartFile file);

    List<Product> searchProduct(String keyword);
    Page<Product> getAllActiveProductPaginated(Integer pageNo, Integer pageSize, String category);
}
