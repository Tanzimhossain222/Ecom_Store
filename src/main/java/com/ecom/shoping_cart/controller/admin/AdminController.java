package com.ecom.shoping_cart.controller.admin;
import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.service.UserService;
import com.ecom.shoping_cart.utils.BucketType;
import com.ecom.shoping_cart.utils.CommonUtils;
import com.ecom.shoping_cart.utils.MailUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInformation userInformation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    MailUtils mailUtils;

    @GetMapping({"", "/"})
    public String index(){
        return "admin/index";
    }

    @GetMapping("/users")
    public String getAllUsers(@RequestParam("type") Integer type,Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<UserDtls> users=null;
        Page<UserDtls> page = null;

        if (type == 1) {
            page  = userService.getAllUsersPagination("ROLE_USER", pageNo, pageSize, sortBy);
        } else if (type == 2) {
            page  = userService.getAllUsersPagination("ROLE_ADMIN", pageNo, pageSize, sortBy);
        } else {
            users = List.of();
        }

        assert page != null;

        users = page.getContent();

        model.addAttribute("userType",type);
        model.addAttribute("users", users);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("pageSize", page.getSize());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());

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

    @GetMapping("/add-admin")
    public String addAdminPage(){
        return "admin/add_admin";
    }

    @PostMapping("/save-admin")
    public String saveAdmin(@ModelAttribute UserDtls userDtls, @RequestParam("img") MultipartFile file, Model model){
        try {
        Boolean check = userService.emailExist(userDtls.getEmail());
        if(check){
            model.addAttribute("errorMsg", "Email already exist");
            return "admin/add_admin";
        }
        userDtls.setRole("ROLE_ADMIN");

        UserDtls user = userService.saveUser(userDtls, file);

        if(user == null){
            model.addAttribute("errorMsg", "Something went wrong");
            return "admin/add_admin";
        }

        mailUtils.registerAdminMail(user.getEmail(), userDtls.getPassword());

        model.addAttribute("successMsg", "Admin added successfully");


        }  catch (Exception e){
            model.addAttribute("errorMsg", "Something went wrong");
            return "admin/add_admin";
        }

        return "redirect:/admin/users?type=2";
    }


    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model){
        try{
            UserDtls user = userInformation.getUserDetails(principal);
            model.addAttribute("user", user);
            return "admin/profile";
        } catch (Exception e){
            model.addAttribute("errorMsg", "Something went wrong");
            return "admin/profile";
        }

    }

    @PostMapping("/update-profile")
    public String updateProfile(UserDtls userDtls,@RequestParam(value = "img",required = false) MultipartFile image, Model model){
        try{
            UserDtls userDtls1 = userService.updateUserProfile(userDtls, image);
            if(userDtls1 == null){
                model.addAttribute("errorMsg", "Something went wrong");
                return "admin/profile";
            }
            model.addAttribute("successMsg", "Profile updated successfully");
            return "redirect:/admin/profile";

        } catch (Exception e){
            model.addAttribute("errorMsg", "Something went wrong");
            return "admin/profile";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(Principal principal, @RequestParam String newPassword, @RequestParam String currentPassword, HttpSession session){
        UserDtls user = userInformation.getUserDetails(principal);

        Boolean match = passwordEncoder.matches(currentPassword, user.getPassword());

        if(match){
            user.setPassword(passwordEncoder.encode(newPassword));
            UserDtls userDtls = userService.updateUser(user);

            if (userDtls != null) {

                session.setAttribute("successMsg", "Password changed successfully");
            } else {
                session.setAttribute("errorMsg", "Failed to change password");
            }

        }else{
            session.setAttribute("errorMsg", "Current password is incorrect");
            return "admin/profile";
        }

        return "redirect:/admin/profile";
    }

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("id") Integer id, @RequestParam(value = "type", defaultValue = "1") Integer type, HttpSession session){
        try{
            Boolean result = userService.deleteUser(id);

            if(result){
                session.setAttribute("successMsg", "User deleted successfully");
            } else {
                session.setAttribute("errorMsg", "Something went wrong");
            }
        }catch (Exception e){
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/users?type="+type;
    }

    @GetMapping("/user/search")
    public String searchUser(@RequestParam("keyword") String keyword, @RequestParam("type") Integer type, Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<UserDtls> users=null;
        Page<UserDtls> page = null;

        if (type == 1) {
            page = userService.getAllSearchUsersPagination("ROLE_USER", keyword, pageNo, pageSize, sortBy);
        } else if (type == 2) {
            page = userService.getAllSearchUsersPagination("ROLE_ADMIN", keyword, pageNo, pageSize, sortBy);
        } else {
            users = List.of();
        }

        if (ObjectUtils.isEmpty(page)) {
            model.addAttribute("errorMsg", "No user found");

        }else {

            users = page.getContent();


            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("pageNo", page.getNumber());
            model.addAttribute("pageSize", page.getSize());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("isFirst", page.isFirst());
            model.addAttribute("isLast", page.isLast());

        }
            model.addAttribute("users", users);
            model.addAttribute("searchActive", true);
            model.addAttribute("keyword", keyword);
            model.addAttribute("userType", type);




        return "admin/users";

    }



}
