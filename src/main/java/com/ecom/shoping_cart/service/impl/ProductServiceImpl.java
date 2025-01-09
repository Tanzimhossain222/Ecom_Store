package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.repository.ProductRepository;
import com.ecom.shoping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    public List<Product> getAllActiveProduct(String category) {
        List<Product> products;

        if(category.isEmpty() || category == null){
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findByIsActiveTrueAndCategory(category);
        }

        return products;
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

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile file)  {

            Product dbProduct = productRepository.findById(product.getId()).orElse(null);

            if (dbProduct == null) {
                return null;
            }

            String imageName = file.isEmpty() ? dbProduct.getImage() : file.getOriginalFilename();
            dbProduct.setTitle(product.getTitle());
            dbProduct.setPrice(product.getPrice());
            dbProduct.setCategory(product.getCategory());
            dbProduct.setIsActive(product.getIsActive());
            dbProduct.setStock(product.getStock());
            dbProduct.setDescription(product.getDescription());
            dbProduct.setDiscount(product.getDiscount());



        if (product.getDiscount() > 0) {
            Double discountPrice = product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
            dbProduct.setDiscountPrice(Double.valueOf(String.format("%.2f", discountPrice)));
        } else {
            dbProduct.setDiscountPrice(Double.valueOf(String.format("%.2f", product.getPrice())));
        }


            dbProduct.setImage(imageName);

            Product updatedProduct = productRepository.save(dbProduct);

           try {
               if (updatedProduct != null) {
                   if (!file.isEmpty()) {
                       File saveDir = new ClassPathResource("static/image/product_img").getFile();
                       if (!saveDir.exists()) {
                           saveDir.mkdirs();
                       }

                       Path path = Path.of(saveDir.getAbsolutePath(), imageName);
                       Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                   }
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

        return updatedProduct;
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword, keyword);
        if (products.isEmpty()) {
            return List.of();
        }
        return products;
    }

    @Override
    public Page<Product> getAllActiveProductPaginated(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable= PageRequest.of(pageNo, pageSize);
        Page<Product> pageProduct = null;

        if(ObjectUtils.isEmpty(category)){
            pageProduct = productRepository.findByIsActiveTrue(pageable);
        }else {
            pageProduct = productRepository.findByIsActiveTrueAndCategory(pageable ,category);
        }


        return pageProduct;

    }

}
