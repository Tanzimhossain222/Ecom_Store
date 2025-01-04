package com.ecom.shoping_cart.controller;

import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

}
