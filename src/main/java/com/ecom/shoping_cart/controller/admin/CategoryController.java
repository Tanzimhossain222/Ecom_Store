package com.ecom.shoping_cart.controller.admin;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.utils.BucketType;
import com.ecom.shoping_cart.utils.CommonUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    private   FileService fileService;

    @Autowired
    CommonUtils commonUtils;


    @GetMapping
    public String categories(    Model m, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<Category> categories = null;
        Page<Category> page = categoryService.getAllCategoryPaginated(pageNo, pageSize);
        categories = page.getContent();

        m.addAttribute("categories", categories);
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", page.getSize());
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());


        return "admin/add_category";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session) {

        String imageUrl = commonUtils.generateImageUrl(file, BucketType.CATEGORY);
        category.setImageName(imageUrl);

        try {

            if (categoryService.existCategory(category.getName())) {
                session.setAttribute("errorMsg", "Category already exists");
                return "redirect:/admin/category";
            }

            Category savedCategory = categoryService.saveCategory(category);

            if(ObjectUtils.isEmpty(savedCategory)){
                session.setAttribute("errorMsg", "Failed to save category");
            }else{
                if (!file.isEmpty()) {
                    fileService.uploadFileS3(file, BucketType.CATEGORY);
                }
                session.setAttribute("successMsg", "Category added successfully");
            }

        } catch (Exception e) {
            session.setAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
        }

        return "redirect:/admin/category";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, HttpSession session) {
        if (categoryService.deleteCategory(id)) {
            session.setAttribute("successMsg", "Category deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Failed to delete category");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model m){
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/admin/category";
        }

        m.addAttribute("category", category);
        return "admin/edit_category";
    }



    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());

        if (oldCategory == null) {
            session.setAttribute("errorMsg", "Category not found");
            return "redirect:/admin/category";
        }

        if (!file.isEmpty()) {

            // Delete the old image from S3
            if (oldCategory.getImageName() != null) {
                fileService.deleteFileS3(oldCategory.getImageName(), BucketType.CATEGORY);
            }

            String imageUrl = commonUtils.generateImageUrl(file, BucketType.CATEGORY);
            oldCategory.setImageName(imageUrl);
        } else {
            oldCategory.setImageName(oldCategory.getImageName());
        }

        oldCategory.setName(category.getName());
        oldCategory.setIsActive(category.getIsActive());

        Category updatedCategory = categoryService.saveCategory(oldCategory);

        if (updatedCategory != null) {
            if (!file.isEmpty()) {
                fileService.uploadFileS3(file, BucketType.CATEGORY);
            }
            session.setAttribute("successMsg", "Category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Failed to update category");
        }

        return "redirect:/admin/category";
    }

}
