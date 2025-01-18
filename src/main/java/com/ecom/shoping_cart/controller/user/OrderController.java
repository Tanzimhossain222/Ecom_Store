package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.ProductOrder;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.CartService;
import com.ecom.shoping_cart.service.OrderService;
import com.ecom.shoping_cart.utils.MailUtils;
import com.ecom.shoping_cart.utils.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
   private UserInformation userInformation;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    MailUtils mailUtils;


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
    public String saveOrder(Principal principal, @ModelAttribute OrderRequest orderRequest, Model model){
        try{
            UserDtls user = userInformation.getUserDetails(principal);
            orderService.saveOrder(user.getId(), orderRequest);
            return "user/success";
        } catch (Exception e){
            model.addAttribute("errorMsg", "Failed to place order");
            return "user/order";
        }
    }

    @GetMapping("/list")
    public String orderList(Principal principal, Model model){
        UserDtls user = userInformation.getUserDetails(principal);
        List<ProductOrder> productOrders = orderService.getOrderByUser(user.getId());


        productOrders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
        model.addAttribute("orders", productOrders);

        return "user/my_orders";
    }

    @GetMapping("/cancel")
    public String cancelOrder(Principal principal, @RequestParam(value = "orderId") Integer orderId, @RequestParam(value = "st") String status, HttpSession session){
        UserDtls user = userInformation.getUserDetails(principal);

        OrderStatus orderStatus = null;

        try {
            Integer statusId = Integer.parseInt(status);
            for (OrderStatus os : OrderStatus.values()) {
                if (os.getId().equals(statusId)) {
                    orderStatus = os;
                    break;
                }
            }

            if (orderStatus == null) {
                throw new IllegalArgumentException("Invalid Order Status ID: " + statusId);
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMsg", "Invalid status value");
            return "redirect:/user/order/list";
        }

        ProductOrder  res = orderService.updateOrderStatus(user.getId(), orderId, orderStatus.name());

      if (!(res == null)){
            mailUtils.sendMailForProductOrder(res, orderStatus.getStatus());
            session.setAttribute("successMsg", "Order Cancelled Successfully");
      } else {
            session.setAttribute("errorMsg", "Order Cancelled Failed");
        }

        return "redirect:/user/order/list";
    }

}
