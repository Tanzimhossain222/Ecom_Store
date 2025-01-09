package com.ecom.shoping_cart.utils;

public enum EmailTemplate {

    RESET_PASSWORD("Reset Password",
            "<p>Dear User,</p>"
                    + "<p>Please click the link below to reset your password:</p>"
                    + "<p><a href=\"[[url]]\">Reset Password</a></p>"
                    + "<br>"
                    + "<p>Thank you</p>"),

    USER_REGISTRATION("User Registration",
            "<p>Dear [[name]],</p>"
                    + "<p>Thank you for registering with us. Your account is created successfully.</p>"
                    + "<br>"
                    + "<p>Thank you</p>"),

    TWO_FACTOR_AUTHENTICATION("Two Factor Authentication",
            "<p>Dear [[name]],</p>"
                    + "<p>Your OTP for two factor authentication is: <b>[[otp]]</b></p>"
                    + "<br>"
                    + "<p>Thank you</p>"),



    PRODUCT_ORDER("Product Order Status",
            "<p>Hello [[name]],</p>"
                    + "<p>Thank you for your order. Your order status is: <b>[[orderStatus]]</b>.</p>"
                    + "<p><b>Product Details:</b></p>"
                    + "<p>Name : [[productName]]</p>"
                    + "<p>Category : [[category]]</p>"
                    + "<p>Quantity : [[quantity]]</p>"
                    + "<p>Price : [[price]]</p>"
                    + "<p>Payment Type : [[paymentType]]</p>");






    private final String subject;
    private final String template;

    EmailTemplate(String subject, String template) {
        this.subject = subject;
        this.template = template;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplate() {
        return template;
    }
}
