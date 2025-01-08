package com.ecom.shoping_cart.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@AllArgsConstructor
    @NoArgsConstructor
@ToString
    @Getter
    @Setter
    @Entity
    public class ProductOrder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String orderId;

        private LocalDate orderDate;

        @ManyToOne
        private Product product;

        private Double price;

        private Integer quantity;

        @ManyToOne
        private UserDtls user;

        private String status;

        private String paymentType;

        @OneToOne(cascade = CascadeType.ALL)
        private OrderAddress orderAddress;

}