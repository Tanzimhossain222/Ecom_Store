package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.repository.CategoryRepository;
import com.ecom.shoping_cart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Category> getAllActiveCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }
}
