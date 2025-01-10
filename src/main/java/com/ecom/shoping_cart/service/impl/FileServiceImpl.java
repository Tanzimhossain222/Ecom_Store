package com.ecom.shoping_cart.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.utils.BucketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    public AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.category}")
    private String categoryBucket;

    @Value("${aws.s3.bucket.product}")
    private String productBucket;

    @Value("${aws.s3.bucket.profile}")
    private String profileBucket;

    private String bucketName;

    @Override
    public Boolean uploadFileS3(MultipartFile file, BucketType bucketType) {
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

        try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);

            PutObjectResult result = amazonS3.putObject(putObjectRequest);

            return !ObjectUtils.isEmpty(result);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in uploading file to S3", e);
        }
    }


    @Override
    public Boolean deleteFileS3(String fileName, BucketType bucketType) {
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

        try {
            // Extract the key from the URL
            String key = fileName.replace("https://" + bucketName + ".s3.eu-north-1.amazonaws.com/", "");

            System.out.println("Deleting file from S3");
            System.out.println("Bucket Name: " + bucketName);
            System.out.println("Key: " + key);

            amazonS3.deleteObject(bucketName, key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}