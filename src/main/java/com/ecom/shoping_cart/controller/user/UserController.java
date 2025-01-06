package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }


}
