package com.ecom.shoping_cart.utils;

public enum OrderStatus {
    IN_PROGRESS(1, "In Progress"),
    ORDER_RECEIVED(2, "Order Received"),
    PRODUCT_PACKED(3, "Product Packed"),
    OUT_FOR_DELIVERY(4, "Out for Delivery"),
    DELIVERED(5, "Delivered"),
    CANCELLED(6, "Cancelled");

    private Integer id;
    private String status;

    OrderStatus(int i, String s) {
        this.id = i;
        this.status = s;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
