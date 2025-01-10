package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.utils.BucketType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Boolean uploadFileS3(MultipartFile file, BucketType bucketType);
    Boolean deleteFileS3(String fileName, BucketType bucketType);
}
