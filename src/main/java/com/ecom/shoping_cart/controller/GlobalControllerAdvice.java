package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.CartService;
import com.ecom.shoping_cart.service.CategoryService;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    CartService cartService;

    @Value("${spring.datasource.url}")
    public static String key;

    @Value("${spring.datasource.username}")
    public static String uname;

    @Value("${spring.datasource.password}")
    public static String password;



    @ModelAttribute
    public void getDbDetails(Model model) {
        System.out.println("Key: " + key);
        System.out.println("uname: " + uname);
        System.out.println("Password: " + password);
    }



    @ModelAttribute
    public void getUserDetails(Principal p, Model model) {
        if (p != null) {
            String email = p.getName();
            model.addAttribute("user", userService.getUserByEmail(email));
        }
    }

    @ModelAttribute
    public void getCategoryList(Model model) {
        model.addAttribute("categories", categoryService.getAllActiveCategory());
    }

    @ModelAttribute
    public void getCartCount(Principal p, Model model) {
        if (p != null) {
            String email = p.getName();
            Integer cartCount = cartService.getCartCount(userService.getUserByEmail(email).getId());

            model.addAttribute("cartCount", (cartCount == null) ? 0 : cartCount);
        }
    }

}