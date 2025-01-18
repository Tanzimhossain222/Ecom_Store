package com.ecom.shoping_cart.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class CommonUtils {


    @Value("${aws.s3.bucket.category}")
    private String categoryBucket;

    @Value("${aws.s3.bucket.product}")
    private String productBucket;

    @Value("${aws.s3.bucket.profile}")
    private String profileBucket;

    @Value("${aws.region}")
    private String region;

    @Autowired
    private JavaMailSender mailSender;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String generateURL(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public String generateImageUrl(MultipartFile file, BucketType bucketType){

        String bucketName;

        switch (bucketType) {
            case CATEGORY:
                bucketName = categoryBucket;
                break;
            case PRODUCT:
                bucketName = productBucket;
                break;
            case PROFILE:
                bucketName = profileBucket;
                break;
            default:
                throw new IllegalArgumentException("Invalid bucket type");
        }

        String fileName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";

        return "https://"+bucketName+".s3."+region+".amazonaws.com/"+fileName;

    }

}
