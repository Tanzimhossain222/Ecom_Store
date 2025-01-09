package com.ecom.shoping_cart.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommonUtils {

    @Autowired
    private JavaMailSender mailSender;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String generateURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

}
