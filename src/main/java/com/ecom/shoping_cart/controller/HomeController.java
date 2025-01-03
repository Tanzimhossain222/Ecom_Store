package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }

    @GetMapping("/products")
    public String product(Model model){

        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProduct();

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "products/index";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model){
        Product product = productService.getProductById(id);
        System.out.println(product);
        return "products/view_product";
    }

}
