package com.ecom.shoping_cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("addProduct")
    public String addProducts(){
        return "admin/add_product";
    }

    @GetMapping("category")
    public String categorie(){
        return "admin/add_category";
    }


    @GetMapping("/")
    public String index(){
        return "admin/index";
    }




}
