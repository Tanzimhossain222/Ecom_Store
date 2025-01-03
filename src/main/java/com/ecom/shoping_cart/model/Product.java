package com.ecom.shoping_cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(length = 200)
    private String title;

    @Column(length = 5000)
    private String description;

    private  Double price;
    private  Integer stock;
    private  String image;
    private  Boolean isActive;
    private String category;
    private Integer discount;
    private Double discountPrice;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", isActive=" + isActive +
                ", category='" + category + '\'' +
                '}';
    }
}
