package com.ecom.shoping_cart.controller.admin;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping({"", "/"})
    public String index(){
        return "admin/index";
    }

    @GetMapping("/users")
    public String getAllUsers(@RequestParam("type") Integer type,Model model){

        List<UserDtls> users;

        if (type == 1) {
            users = userService.getAllUsers("ROLE_USER");
        } else if (type == 2) {
            users = userService.getAllUsers("ROLE_ADMIN");
        } else {
            users = List.of();
        }

        model.addAttribute("userType",type);
        model.addAttribute("users", users);
        return "admin/users";
    }


    @GetMapping("/update-status")
    public String updateUserAccountStatus(@RequestParam("type") Integer type, @RequestParam("id") Integer id, @RequestParam("status") Boolean status, HttpSession session){
        Boolean result = userService.updateAccountStatus(id, status);

        if(result){
            session.setAttribute("successMsg", "User status updated successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/users?type="+type;
    }



}
