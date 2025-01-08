package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.UserService;
import com.ecom.shoping_cart.utils.CommonUtils;
import com.ecom.shoping_cart.utils.MailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MailUtils mailUtils;

    @GetMapping("/signin")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }

    @RequestMapping("/saveUser")
    public String saveUser(@ModelAttribute UserDtls userDtls, @RequestParam("img") MultipartFile file, Model model){
        String imageName=   file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        userDtls.setProfileImage(imageName);

        UserDtls user = userService.saveUser(userDtls);
        if(user == null){
            model.addAttribute("errorMsg", "Something went wrong");
            return "auth/register";
        }

        try {
            File staticDir = new ClassPathResource("static").getFile();
            File profileImgDir = new File(staticDir, "image/profile_img");

            if (!profileImgDir.exists()) {
                profileImgDir.mkdirs();
            }

            Path path = Paths.get(profileImgDir.getAbsolutePath() + File.separator + file.getOriginalFilename());

            System.out.println("Path: " + path);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("successMsg", "User registered successfully");


        return  "redirect:/register";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword( ) {

        return "auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, HttpSession session, HttpServletRequest request) {
        UserDtls userByEmail = userService.getUserByEmail(email);

        if (userByEmail == null) {

            session.setAttribute("errorMsg", "User not found with this email !!");
        } else {

           try{
               String token = commonUtils.generateToken();
               userService.updateResetToken(token, email);

               //generate URL: http://localhost:8080/reset-password?token=123456
               String url =    commonUtils.generateURL(request);
               url = url + "/reset-password?token=" + token;

               Boolean sendMail=   mailUtils.sendEmail( email, url);

               if (sendMail) {

                   session.setAttribute("successMsg", "Reset link sent to your email !!");
               } else {

                   session.setAttribute("errorMsg", "Something went wrong !! Mail not sent !!");
               }
           } catch (Exception e){
               e.printStackTrace();
               session.setAttribute("errorMsg", "Something went wrong !! Mail not sent !!");
           }

        }

        return "redirect:/forgot-password";
    }



    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam("token") String token, Model model) {

        UserDtls user = userService.getUserByToken(token);

        if (user == null) {
            model.addAttribute("msg", "Invalid token !!");
            return "message";
        }

        model.addAttribute("token", token);
        return "auth/reset_password";
    }


    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword, Model model) {

        UserDtls user = userService.getUserByToken(token);

        if (user == null) {
            model.addAttribute("msg", "Invalid token !!");
            return "message";
        }

        Boolean res =  userService.updatePassword(user.getId(), newPassword);
        System.out.println("Result: " + res);

        if (!res) {
            model.addAttribute("msg", "Something went wrong !!");
            return "message";
        }

        model.addAttribute("msg", "Password updated successfully !!");
        return "message";

    }


}
