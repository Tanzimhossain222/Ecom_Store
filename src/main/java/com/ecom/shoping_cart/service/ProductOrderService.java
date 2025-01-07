package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.model.ProductOrder;

public interface ProductOrderService {
    void saveOrder(Integer userId, OrderRequest orderRequest);
}
