package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.CartService;
import com.ecom.shoping_cart.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
   private UserInformation userInformation;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private CartService cartService;


    private Double[] calculateTaxAndShipping(Double totalOrderPrice) {
    Double tax;
    Double shipping;

    if (totalOrderPrice < 1000) {
        tax = 0.005 * totalOrderPrice;
    } else if (totalOrderPrice < 5000) {
        tax = 0.01 * totalOrderPrice;
    } else {
        tax = 0.5 * totalOrderPrice;
    }

    if (totalOrderPrice < 500) {
        shipping = 5.0;
    } else if (totalOrderPrice < 5000) {
        shipping = 10.0;
    } else {
        shipping = 15.0;
    }

    return new Double[]{tax, shipping};
}

@GetMapping({"", "/"})
public String orderPage(Principal principal, Model model) {
    UserDtls user = userInformation.getUserDetails(principal);
    List<Cart> carts = cartService.getCartByUser(user.getId());

    if (!carts.isEmpty()) {
        Double orderPrice = carts.stream().mapToDouble(Cart::getTotalPrice).sum();
        Double[] taxAndShipping = calculateTaxAndShipping(orderPrice);

        Double tax = taxAndShipping[0];
        Double shipping = taxAndShipping[1];
        Double totalOrderPrice = orderPrice + tax + shipping;

        model.addAttribute("OrderPrice", String.format("%.2f", orderPrice));
        model.addAttribute("tax", String.format("%.2f", tax));
        model.addAttribute("shipping", String.format("%.2f", shipping));
        model.addAttribute("totalOrderPrice", String.format("%.2f", totalOrderPrice));
    } else {
        model.addAttribute("errorMsg", "Your cart is empty");
    }

    return "user/order";
}

    @PostMapping("/save")
    public String saveOrder(Principal principal, @ModelAttribute OrderRequest orderRequest){
        UserDtls user = userInformation.getUserDetails(principal);
        productOrderService.saveOrder(user.getId(), orderRequest);

        return "user/success";
    }


}
