package com.ecom.shoping_cart.controller.admin;

import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.ProductOrder;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.OrderService;
import com.ecom.shoping_cart.utils.MailUtils;
import com.ecom.shoping_cart.utils.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
    public String orderList(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        Page<ProductOrder> page = orderService.getAllOrdersPaginated(pageNo, pageSize);

        List<ProductOrder> allOrders = page.getContent();

        model.addAttribute("orders", allOrders);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());


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
    public String searchOrder(@RequestParam("orderId") String orderId, Model model, HttpSession session,
                              @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        if (orderId != null && orderId.length() > 0) {

            ProductOrder order = orderService.getOrderById(orderId.trim());
            System.out.println("Order: " + order);

            if (ObjectUtils.isEmpty(order)) {
                session.setAttribute("errorMsg", "Incorrect orderId");
                model.addAttribute("orderDtls", null);
            } else {
                model.addAttribute("orderDtls", order);
            }

            model.addAttribute("searchActive", true);
        }else {

            Page<ProductOrder> page = orderService.getAllOrdersPaginated(pageNo, pageSize);
            List<ProductOrder> allOrders = page.getContent();
            System.out.println("All Orders: " + allOrders);

            model.addAttribute("orders", allOrders);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("pageNo", page.getNumber());
            model.addAttribute("pageSize", page.getSize());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("isFirst", page.isFirst());
            model.addAttribute("isLast", page.isLast());
            model.addAttribute("searchActive", false);

        }

        return "admin/oders";

    }



}

