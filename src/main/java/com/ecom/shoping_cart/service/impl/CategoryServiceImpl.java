package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.repository.CategoryRepository;
import com.ecom.shoping_cart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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
            //also remove image from server
            try {
                // Remove the image file from the server
                File imageDir = new ClassPathResource("static/image/category_img").getFile();
                File imageFile = new File(imageDir, category.getImageName());

                if (imageFile.exists() && !category.getImageName().equals("default.jpg")) {
                    if (!imageFile.delete()) {
                        System.err.println("Failed to delete image: " + imageFile.getAbsolutePath());
                    }
                }

                categoryRepository.delete(category);
                return true;
            }catch (IOException e) {
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
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories;
    }
}
