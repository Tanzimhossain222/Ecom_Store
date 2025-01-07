package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.model.ProductOrder;

import java.util.List;

public interface ProductOrderService {
    void saveOrder(Integer userId, OrderRequest orderRequest);
    List<ProductOrder> getOrderByUser(Integer userId);

    Boolean cancelOrder(Integer id, Integer orderId, String status);
}
