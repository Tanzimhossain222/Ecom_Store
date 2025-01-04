package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.Category;
import com.ecom.shoping_cart.model.Product;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.ProductService;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/products")
    public String product(Model model, @RequestParam(value = "category", required = false, defaultValue = "") String category){

        List<Category> categories = categoryService.getAllActiveCategory();

        List<Product> products = productService.getAllActiveProduct(category);

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("paramValue", category);

        return "products/index";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        return "products/view_product";
    }

}
