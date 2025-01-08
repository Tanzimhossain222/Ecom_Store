package com.ecom.shoping_cart.controller.admin;

import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.ProductOrder;
import com.ecom.shoping_cart.model.UserDtls;
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
@RequestMapping("/admin/order")
public class OderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserInformation userInformation;

    @Autowired
    private MailUtils mailUtils;

    @GetMapping("/list")
    public String orderList(Model model) {
        List<ProductOrder> allOrders = orderService.getAllOrders();

        model.addAttribute("orders", allOrders);
        model.addAttribute("searchActive", false);
        return "admin/oders";
    }


    @PostMapping("/update-status")
    public String updateOrderStatus(Principal principal,@RequestParam(value = "id") Integer orderId, @RequestParam(value = "st") String status, HttpSession session) {
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

        ProductOrder res = orderService.updateOrderStatus(user.getId(), orderId, orderStatus.name());

        if (!(res == null)) {

            mailUtils.sendMailForProductOrder(res, orderStatus.getStatus());

            session.setAttribute("successMsg", "Order status updated successfully");
        } else {
            session.setAttribute("errorMsg", "Order status not updated");
        }

        return "redirect:/admin/order/list";
    }

    @GetMapping("/search")
    public String searchOrder(@RequestParam("productId") String productId, Model model) {


        ProductOrder orderById = orderService.getOrderById(productId);

        if (!(orderById == null)) {
            model.addAttribute("searchActive", true);
            model.addAttribute("orderDtls", orderById);
        } else {
            model.addAttribute("errorMsg", "Order not found");
        }

        return "admin/oders";

    }



}

