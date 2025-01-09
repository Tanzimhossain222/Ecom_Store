package com.ecom.shoping_cart.lib;

import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserInformation {

    @Autowired
    UserService userService;
    public UserDtls getUserDetails(Principal p) {
        if (p != null) {
            String email = p.getName();
            return userService.getUserByEmail(email);
        }
        return null;
    }
}
