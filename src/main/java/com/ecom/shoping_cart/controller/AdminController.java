package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("addProduct")
    public String addProducts(){
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String categorie(Model m){
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session) {
        String fileName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(fileName);

        try {

            if (categoryService.existCategory(category.getName())) {
                session.setAttribute("errorMsg", "Category already exists");
                return "redirect:/admin/category";
            }

            Category savedCategory = categoryService.saveCategory(category);
            if (savedCategory != null) {
                // Save the file to the server
                if (file != null && !file.isEmpty()) {
                    try {
                        File saveDir = new ClassPathResource("static/image/category_img").getFile();
                        if (!saveDir.exists()) {
                            saveDir.mkdirs();
                        }

                        Path path = Path.of(saveDir.getAbsolutePath(), fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        session.setAttribute("errorMsg", "Category saved, but file upload failed: " + e.getMessage());
                        return "redirect:/admin/category";
                    }
                }

                session.setAttribute("successMsg", "Category added successfully");
            } else {
                session.setAttribute("errorMsg", "Failed to save category");
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, HttpSession session) {
        if (categoryService.deleteCategory(id)) {
            session.setAttribute("successMsg", "Category deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Failed to delete category");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model m){
        Category category = categoryService.getCategoryById(id);

        if (category == null) {
            return "redirect:/admin/category";
        }

        m.addAttribute("category", category);
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());

        if (oldCategory == null) {
            session.setAttribute("errorMsg", "Category not found");
            return "redirect:/admin/category";
        }

        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        oldCategory.setName(category.getName());
        oldCategory.setImageName(imageName);
        oldCategory.setIsActive(category.getIsActive());

        Category updatedCategory = categoryService.saveCategory(oldCategory);

        if (updatedCategory != null) {
            if (!file.isEmpty()) {
                File saveDir = new ClassPathResource("static/image/category_img").getFile();
                System.out.println("saveDir = " + saveDir.getAbsolutePath());
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                Path path = Path.of(saveDir.getAbsolutePath(), imageName);
                System.out.println("path = " + path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("successMsg", "Category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Failed to update category");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

}
