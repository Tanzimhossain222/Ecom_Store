package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(Integer userId);
    ProductOrder findByOrderId(String id);


//    @Query("SELECT o FROM ProductOrder o WHERE " +
//            "(LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(o.orderAddress.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(o.orderAddress.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(o.orderAddress.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(o.orderAddress.mobileNo) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
//            "(:status IS NULL OR o.status = :status)")
//    Page<ProductOrder> searchOrders(@Param("keyword") String keyword,
//                                    @Param("status") String status,
//                                    Pageable pageable);

}
