package com.ecom.shoping_cart.utils;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public Boolean sendEmail(String recipientEmail, String url) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("kdptanzim@gmail.com", "Shopping Cart");
            helper.setTo(recipientEmail);

            String subject = "Reset Password";
            String content = "<p>Dear User,</p>"
                    + "<p>Please click the link below to reset your password:</p>"
                    + "<p><a href=\"" + url + "\">Reset Password</a></p>"
                    + "<br>"
                    + "<p>Thank you</p>";

            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
