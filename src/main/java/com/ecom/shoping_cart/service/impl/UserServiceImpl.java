package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.utils.AppConstant;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.repository.UserRepository;
import com.ecom.shoping_cart.service.UserService;
import com.ecom.shoping_cart.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    FileUploadUtil fileUploadUtil;

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

    @Override
    public UserDtls updateUser(UserDtls userDtls) {
        return userRepository.save(userDtls);
    }

    @Override
    public UserDtls updateUserProfile(UserDtls user, MultipartFile img) {
        UserDtls dbUser = userRepository.findById(user.getId()).orElse(null);

        if (dbUser == null) {
            return null;
        }


        if (!img.isEmpty()) {

            dbUser.setProfileImage(img.getOriginalFilename());
        }


        dbUser.setName(user.getName());
        dbUser.setMobileNumber(user.getMobileNumber());
        dbUser.setAddress(user.getAddress());
        dbUser.setCity(user.getCity());
        dbUser.setState(user.getState());
        dbUser.setPincode(user.getPincode());
        dbUser = userRepository.save(dbUser);



        try {

            if ( img != null && !img.isEmpty()) {
                File saveDir = new ClassPathResource("static/image/profile_img").getFile();
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                    Path path = Path.of(saveDir.getAbsolutePath(), img.getOriginalFilename());
                System.out.println("Path: " + path);
                    Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




}
