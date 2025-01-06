package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.service.CartService;
import com.ecom.shoping_cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/cart/")
public class CartController {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @GetMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId, @RequestParam("userId") Integer userId, Model model){

        Cart cart = cartService.saveCart(userId, productId);

        if(cart == null){
            model.addAttribute("errorMsg", "Product add to cart failed");

        } else {
            model.addAttribute("successMsg", "Product added to cart");
        }

        System.out.println(cart);

        return "redirect:/product/" + productId;
    }

}
