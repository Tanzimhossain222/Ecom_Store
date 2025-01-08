package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.dto.OrderRequest;
import com.ecom.shoping_cart.model.Cart;
import com.ecom.shoping_cart.model.OrderAddress;
import com.ecom.shoping_cart.model.ProductOrder;
import com.ecom.shoping_cart.repository.CartRepository;
import com.ecom.shoping_cart.repository.OrderRepository;
import com.ecom.shoping_cart.service.OrderService;
import com.ecom.shoping_cart.utils.MailUtils;
import com.ecom.shoping_cart.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MailUtils mailUtils;


    @Override
    public void saveOrder(Integer userId, OrderRequest orderRequest) {
        List<Cart> cartList = cartRepository.findByUserId(userId);

        for (Cart cart : cartList ) {
            // Order Details
            ProductOrder order = new ProductOrder();
            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.name());
            order.setPaymentType(orderRequest.getPaymentType());

            // Order Address
            OrderAddress address = new OrderAddress();

            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            System.out.println("Final Order: " + order);

            orderRepository.save(order);

            // Send Email
            mailUtils.sendMailForProductOrder(order, OrderStatus.SUCCESS.getStatus());

//            cartRepository.delete(cart);
        }
    }

    @Override
    public List<ProductOrder> getOrderByUser(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public ProductOrder updateOrderStatus(Integer id, Integer orderId, String status) {
        ProductOrder order = orderRepository.findById(orderId).orElse(null);
        assert order != null;
        order.setStatus(status);
        ProductOrder save = orderRepository.save(order);

        if (save != null) {
            return save;
        } else {
            return null;
        }

    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public ProductOrder getOrderById(String id) {
        return orderRepository.findByOrderId(id);
    }

}
