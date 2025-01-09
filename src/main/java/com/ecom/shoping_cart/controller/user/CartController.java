package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.CartService;
import com.ecom.shoping_cart.service.ProductService;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @GetMapping
    public String cartPage(Principal p, Model model){
        String email = p.getName();

        UserDtls user = userService.getUserByEmail(email);

        List<Cart> carts = cartService.getCartByUser(user.getId());

        if (carts.isEmpty()) {
            System.out.println("Cart is empty");
            model.addAttribute("errorMsg", "Your cart is empty");
        } else  {
        Double totalOrderPrice = (carts.stream().mapToDouble(Cart::getTotalPrice).sum());

        String formattedTotalOrderPrice = String.format("%.2f", totalOrderPrice);

        model.addAttribute("carts", carts);
        model.addAttribute("totalOrderPrice", formattedTotalOrderPrice);


        }

        return "user/cart";
    }

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


    @GetMapping("/quantityUpdate")
    public String QuantityUpdate(@RequestParam("cardId") Integer cardId, @RequestParam("type") String type, Model model){
        cartService.updateQuantity(cardId, type);
        return "redirect:/user/cart";
    }

}
