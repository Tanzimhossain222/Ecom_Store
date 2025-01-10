package com.ecom.shoping_cart.utils;

public enum BucketType {
    CATEGORY("categoryBucket"),
    PRODUCT("productBucket"),
    PROFILE("profileBucket");

    private final String bucketName;

    BucketType(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}