package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("addProduct")
    public String addProducts(){
        return "admin/add_product";
    }

    @GetMapping("category")
    public String categorie(){
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

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }




}
