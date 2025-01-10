package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.repository.CategoryRepository;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.utils.BucketType;
import com.ecom.shoping_cart.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;


    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            try {
                if (category.getImageName() != null) {
                    fileService.deleteFileS3(category.getImageName(), BucketType.CATEGORY);
                }
                categoryRepository.delete(category);
                return true;
            }catch (Exception e) {
                System.err.println("Error accessing the image directory: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Category> getAllCategoryPaginated(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories;
    }

}
