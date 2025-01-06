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

    void increaseFailedAttempt(UserDtls userDtls);

    void userAccountLock(UserDtls userDtls);

    Boolean unlockAccountTimeExpired(UserDtls userDtls);

    void resetFailedAttempts(Integer userId);

    void updateResetToken(String token, String email);

    UserDtls getUserByToken(String token);

    Boolean updatePassword(Integer id, String newPassword);
}