package com.ecom.shoping_cart.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
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

    @Value("${aws.region}")
    private String region;

    private String bucketName;

    @Override
    public Boolean uploadFileS3(MultipartFile file, BucketType bucketType) {
        try {
            bucketName = getBucketName(bucketType);

            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty. Cannot upload to S3.");
            }

            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);

            PutObjectResult result = amazonS3.putObject(putObjectRequest);

            if (ObjectUtils.isEmpty(result)) {
                throw new AmazonS3Exception("S3 upload did not return a valid response.");
            }
            return true;

        } catch (AmazonS3Exception e) {
            throw new RuntimeException("AWS S3 error during file upload: " + e.getErrorMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred during file upload: " + e.getMessage(), e);
        }
    }


    @Override
    public Boolean deleteFileS3(String fileName, BucketType bucketType) {
        try {
            bucketName = getBucketName(bucketType);

            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("File name cannot be null or empty for deletion.");
            }

            String key = fileName.replace("https://" + bucketName + ".s3." + region + ".amazonaws.com/", "");

            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            return true;
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("AWS S3 error during file deletion: " + e.getErrorMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred during file deletion: " + e.getMessage(), e);
        }
    }


    private String getBucketName(BucketType bucketType) {
        switch (bucketType) {
            case CATEGORY:
                return categoryBucket;
            case PRODUCT:
                return productBucket;
            case PROFILE:
                return profileBucket;
            default:
                throw new IllegalArgumentException("Invalid bucket type: " + bucketType);
        }
    }

}