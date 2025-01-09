package com.ecom.shoping_cart.controller.admin;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.ProductService;
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
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public String loadViewProduct(Model m){
        List<Product> products = productService.getAllProduct();

        if (products == null) {
            m.addAttribute("errorMsg", "No products found");
            return "admin/products";
        }


        m.addAttribute("products", products);
        return "admin/products";
    }


    @GetMapping("/add")
    public String loadAddProducts(Model m){
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }


    @PostMapping("/save")
    public  String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file, HttpSession session){

        String fileName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        product.setImage(fileName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        try {
            Product saveProduct = productService.saveProduct(product);

            if (saveProduct != null) {
                // Save the file to the server
                if (file != null && !file.isEmpty()) {
                    try {
                        File saveDir = new ClassPathResource("static/image/product_img").getFile();
                        if (!saveDir.exists()) {
                            saveDir.mkdirs();
                        }

                        Path path = Path.of(saveDir.getAbsolutePath(), fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        session.setAttribute("errorMsg", "Product saved, but file upload failed: " + e.getMessage());
                        return "redirect:/admin/addProduct";
                    }
                }

                session.setAttribute("successMsg", "Product added successfully");
            } else {
                session.setAttribute("errorMsg", "Failed to save product");
            }

        } catch (Exception e) {
            session.setAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
        }
        return "admin/add_product";
    }


    @GetMapping("/delete")
    public String deleteProduct (@RequestParam("id") Integer id, HttpSession session){

        if (productService.deleteProduct(id)) {
            session.setAttribute("successMsg", "Product deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Failed to delete product");
        }

        return "redirect:/admin/product/list";
    }


    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model m){
        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/admin/products";
        }

        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        m.addAttribute("product", product);
        return "admin/edit_product";
    }


    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException
    {

        if(product.getDiscount() < 0 ||  product.getDiscount() > 100){
            session.setAttribute("errorMsg", "Discount must be between 0 and 100");
            return "redirect:/admin/product/edit/" + product.getId();
        }


        Product oldProduct = productService.updateProduct(product, file);

        if (oldProduct == null) {
            session.setAttribute("errorMsg", "Product not found");
            return "redirect:/admin/product/list";
        }

        session.setAttribute("successMsg", "Product updated successfully");

        return "redirect:/admin/product/list";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("ch") String name, Model m){
        List<Product> products = productService.searchProduct(name);

        if (products == null) {
            m.addAttribute("errorMsg", "No products found");
            return "admin/products";
        }

        m.addAttribute("products", products);
        return "admin/products";
    }


}
