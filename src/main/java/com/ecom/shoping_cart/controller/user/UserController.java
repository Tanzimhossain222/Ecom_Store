package com.ecom.shoping_cart.controller.user;

import com.ecom.shoping_cart.lib.UserInformation;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.ProductService;
import com.ecom.shoping_cart.service.UserService;
import com.ecom.shoping_cart.utils.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInformation userInformation;

    @Autowired
    FileUploadUtil fileUploadUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model){
        UserDtls userDtls = userInformation.getUserDetails(principal);

        model.addAttribute("user", userDtls);
        return "user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(UserDtls userDtls,@RequestParam(value = "img",required = false) MultipartFile image, Model model){
      try {
          UserDtls userDtls1 = userService.updateUserProfile(userDtls, image);
      } catch (Exception e) {
          e.printStackTrace();
          model.addAttribute("errorMsg", "Failed to update profile");
      }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(Principal principal, @RequestParam String newPassword, @RequestParam String currentPassword, HttpSession session){
        UserDtls user = userInformation.getUserDetails(principal);

        boolean match = passwordEncoder.matches(currentPassword, user.getPassword());

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
            return "user/profile";
        }

        return "redirect:/user/profile";
    }


}
