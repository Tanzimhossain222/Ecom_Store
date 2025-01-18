package com.ecom.shoping_cart.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.repository.CartRepository;
import com.ecom.shoping_cart.repository.ProductRepository;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.service.ProductService;
import com.ecom.shoping_cart.utils.BucketType;
import com.ecom.shoping_cart.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private FileService fileService;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product saveProduct(Product product, MultipartFile file) {
        String ImageUrl = commonUtils.generateImageUrl(file, BucketType.PRODUCT);
        product.setImage(ImageUrl);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
           try {
               Product savedProduct = productRepository.save(product);

               Boolean res=   fileService.uploadFileS3(file, BucketType.PRODUCT);
                if (!res) {
                     throw new AmazonS3Exception("Error in saving product image");
                }
                return savedProduct;
           } catch (AmazonS3Exception e ) {
               throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
           } catch (Exception e) {
               throw new RuntimeException("Error in saving product");
           }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllActiveProduct(String category) {
        List<Product> products;

        if(category == null || category.isEmpty()){
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findByIsActiveTrueAndCategory(category);
        }

        return products;
    }

    @Override
    @Transactional
    public boolean deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return false;
        }

        try{
            if (product.getImage() != null) {
                fileService.deleteFileS3(product.getImage(), BucketType.PRODUCT);
            }

            cartRepository.deleteByProduct(product);


            productRepository.delete(product);
            return true;
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in deleting product");
        }

    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile file)  {

            try{
                Product dbProduct = productRepository.findById(product.getId()).orElse(null);

                if (dbProduct == null) {
                    return null;
                }

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

                if (!file.isEmpty()) {
                    if (dbProduct.getImage() != null) {
                        fileService.deleteFileS3(dbProduct.getImage(), BucketType.PRODUCT);
                    }

                    String imageUrl = commonUtils.generateImageUrl(file, BucketType.PRODUCT);
                    dbProduct.setImage(imageUrl);
                } else {
                    dbProduct.setImage(dbProduct.getImage());
                }
                Product updatedProduct = productRepository.save(dbProduct);

                if(ObjectUtils.isEmpty(updatedProduct)){
                    throw new RuntimeException("Error in updating product");
                }

                if (!file.isEmpty()){
                    Boolean res = fileService.uploadFileS3(file, BucketType.PRODUCT);
                    if (!res) {
                        throw new AmazonS3Exception("Error in saving product image");
                    }
                }

                return updatedProduct;
            } catch (AmazonS3Exception e) {
                throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Error in updating product");
            }
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

        if(category == null || category.isEmpty()){
            pageProduct = productRepository.findByIsActiveTrue(pageable);
        }else {
            pageProduct = productRepository.findByIsActiveTrueAndCategory(pageable ,category);
        }


        return pageProduct;

    }

    @Override
    public Page<Product> getAllProductPaginated(Integer pageNo, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProductPaginated(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNo, pageSize);

        Page<Product> res = productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(pageable, keyword, keyword);

        if (res.isEmpty()) {
            return Page.empty();
        }

        return res;

    }

}
