package com.ecom.shoping_cart.utils;

import com.ecom.shoping_cart.model.ProductOrder;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailUtils {

    @Autowired
    private JavaMailSender mailSender;

    public void sentEmail(String email, String sub, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom("kdptanzim@gmail.com", "Shopping Cart");
            helper.setTo(email);
            helper.setSubject(sub);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public Boolean sendResetEmail(String recipientEmail, String url) {
        String subject = "Reset Password";
        String body = "<p>Dear User,</p>"
                + "<p>Please click the link below to reset your password:</p>"
                + "<p><a href=\"" + url + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>Thank you</p>";

        sentEmail(recipientEmail, subject, body);
        return true;
    }

    public Boolean sendMailForProductOrder(ProductOrder order, String status) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try{
            helper.setFrom("kdptanzim@gmail.com", "Shopping Cart");
            String email = order.getOrderAddress().getEmail();
            String name = order.getOrderAddress().getFirstName() + " " + order.getOrderAddress().getLastName();

            helper.setTo(email);

            String messageContent = "<p>Hello [[name]],</p>"
                    + "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
                    + "<p><b>Product Details:</b></p>"
                    + "<p>Name : [[productName]]</p>"
                    + "<p>Category : [[category]]</p>"
                    + "<p>Quantity : [[quantity]]</p>"
                    + "<p>Price : [[price]]</p>"
                    + "<p>Payment Type : [[paymentType]]</p>";

            messageContent = messageContent.replace("[[name]]", name);
            messageContent = messageContent.replace("[[orderStatus]]", status);
            messageContent = messageContent.replace("[[productName]]", order.getProduct().getTitle());
            messageContent = messageContent.replace("[[category]]", order.getProduct().getCategory());
            messageContent = messageContent.replace("[[quantity]]", order.getQuantity().toString());
            messageContent = messageContent.replace("[[price]]", order.getProduct().getPrice().toString());
            messageContent = messageContent.replace("[[paymentType]]", order.getPaymentType());

            helper.setSubject("Product Order Status");

            helper.setText(messageContent, true);

            mailSender.send(message);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public void registerAdminMail(String email, String password) {
        String subject = "Admin Registration";
        String body = "<p>Dear User,</p>"
                + "<p>Your account has been created successfully.</p>"
                + "<p>Now You can Login as Admin</p>"
                + "<p>Email: " + email + "</p>"
                + "<p>Password: " + password + "</p>"
                + "<br>"
                + "<p>Thank you</p>";
        sentEmail(email, subject, body);
    }


}
