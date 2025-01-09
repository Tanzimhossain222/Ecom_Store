package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.UserDtls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    UserDtls findByEmail(String email);

    List<UserDtls> findAllByRole(String role);

    Page<UserDtls> findAllByRole(String role, Pageable pageable);

    UserDtls findByResetToken(String token);


    @Query("SELECT u FROM UserDtls u WHERE u.role = :role AND " +
            "(u.email LIKE %:keyword% OR u.name LIKE %:keyword% OR u.mobileNumber LIKE %:keyword%)")
    Page<UserDtls> findAllByRoleAnd(@Param("role") String role,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);

//    Page<UserDtls> findAllByRoleAndEmailContainingOrNameContainingOrMobileNumberContaining(String role, String keyword, String keyword1, String keyword2, Pageable pageable);
}
