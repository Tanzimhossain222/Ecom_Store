package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public Category saveCategory(Category category);
    public List<Category> getAllActiveCategory();
    public Boolean existCategory(String name);
    public List<Category> getAllCategory();
    public  Boolean deleteCategory(Integer id);

    Category getCategoryById(Integer id);

    Page<Category> getAllCategoryPaginated(Integer pageNo, Integer pageSize);

}
