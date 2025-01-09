package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.model.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void saveOrder(Integer userId, OrderRequest orderRequest);
    List<ProductOrder> getOrderByUser(Integer userId);

    ProductOrder updateOrderStatus(Integer id, Integer orderId, String status);
    List<ProductOrder> getAllOrders();
    ProductOrder getOrderById(String id);
    Page<ProductOrder> getAllOrdersPaginated(Integer pageNo, Integer pageSize);

}
