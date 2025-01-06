package com.ecom.shoping_cart.repository;

import com.ecom.shoping_cart.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    UserDtls findByEmail(String email);

    List<UserDtls> findAllByRole(String role);

    UserDtls findByResetToken(String token);
}
