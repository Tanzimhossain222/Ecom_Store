package com.ecom.shoping_cart.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface CommonService {
    public void removeSessionMessage();
    String getFormattedOrderDate(LocalDateTime orderDate);
}
