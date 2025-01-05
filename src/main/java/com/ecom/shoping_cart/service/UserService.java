package com.ecom.shoping_cart.service;

import com.ecom.shoping_cart.model.UserDtls;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDtls saveUser(UserDtls userDtls);

    UserDtls getUserById(Integer id);

    UserDtls getUserByEmail(String email);

    List<UserDtls> getAllUsers(String role);

    Boolean updateAccountStatus(Integer id, Boolean status);
}
