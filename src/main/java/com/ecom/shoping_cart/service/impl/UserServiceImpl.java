package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.utils.AppConstant;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.repository.UserRepository;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDtls saveUser(UserDtls userDtls) {
        userDtls.setRole("ROLE_USER");
        userDtls.setIsEnable(true);
        userDtls.setAccountNonLocked(true);
        userDtls.setFailedAttempt(0);

        String encodedPassword = passwordEncoder.encode(userDtls.getPassword());
        userDtls.setPassword(encodedPassword);
        return userRepository.save(userDtls);
    }

    @Override
    public UserDtls getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDtls> getAllUsers(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        UserDtls user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        user.setIsEnable(status);
        userRepository.save(user);
        return true;

    }

    @Override
    public void increaseFailedAttempt(UserDtls userDtls) {
        int newFailAttempts = userDtls.getFailedAttempt() + 1;
        userDtls.setFailedAttempt(newFailAttempts);
        userRepository.save(userDtls);
    }

    @Override
    public void userAccountLock(UserDtls userDtls) {
        userDtls.setAccountNonLocked(false);
        userDtls.setLockTime(new Date());
        userRepository.save(userDtls);

    }

    @Override
    public Boolean unlockAccountTimeExpired(UserDtls user) {
        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }

        return false;

    }

    @Override
    public void resetFailedAttempts(Integer userId) {
        UserDtls user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFailedAttempt(0);
            userRepository.save(user);
        }
    }

    @Override
    public void updateResetToken(String token, String email) {
        UserDtls userByEmail = userRepository.findByEmail(email);
        if (userByEmail != null) {
            userByEmail.setResetToken(token);
            userRepository.save(userByEmail);
        }
    }

    @Override
    public UserDtls getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public Boolean updatePassword(Integer id, String newPassword) {
        UserDtls user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        return true;
    }


}
