package com.ecom.shoping_cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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
    public String product(){
        return "products/index";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(){
        return "products/view_product";
    }

}
