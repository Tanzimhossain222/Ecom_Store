package com.ecom.shoping_cart.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ecom.shoping_cart.service.FileService;
import com.ecom.shoping_cart.utils.AppConstant;
import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.repository.UserRepository;
import com.ecom.shoping_cart.service.UserService;
import com.ecom.shoping_cart.utils.BucketType;
import com.ecom.shoping_cart.utils.CommonUtils;
import com.ecom.shoping_cart.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Lazy
    private CommonUtils commonUtils;

    @Autowired
    FileService fileService;

    @Override
    public UserDtls saveUser(UserDtls userDtls) {
        return null;
    }

    @Override
    public UserDtls saveUser(UserDtls userDtls, MultipartFile file) {
        userDtls.setIsEnable(true);
        userDtls.setAccountNonLocked(true);
        userDtls.setFailedAttempt(0);

        String imageUrl = commonUtils.generateImageUrl(file, BucketType.PROFILE);
        userDtls.setProfileImage(imageUrl);
        String encodedPassword = passwordEncoder.encode(userDtls.getPassword());
        userDtls.setPassword(encodedPassword);

        try {
            UserDtls save = userRepository.save(userDtls);

            Boolean res=   fileService.uploadFileS3(file, BucketType.PROFILE);
            if (!res) {
                throw new AmazonS3Exception("Error in saving product image");
            }
            return save;
        } catch (AmazonS3Exception e ) {
            throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error in saving product");
        }

    }

    @Override
    public Boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
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

        dbUser.setName(user.getName());
        dbUser.setMobileNumber(user.getMobileNumber());
        dbUser.setAddress(user.getAddress());
        dbUser.setCity(user.getCity());
        dbUser.setState(user.getState());
        dbUser.setPincode(user.getPincode());


        if (!img.isEmpty()) {
            if(dbUser.getProfileImage() != null) {
                fileService.deleteFileS3(dbUser.getProfileImage(), BucketType.PROFILE);
            }
            String imageUrl = commonUtils.generateImageUrl(img, BucketType.PROFILE);
            dbUser.setProfileImage(imageUrl);
        } else {
            dbUser.setProfileImage(dbUser.getProfileImage());
        }
        dbUser = userRepository.save(dbUser);

        try {
            if (!img.isEmpty()) {
                Boolean res=   fileService.uploadFileS3(img, BucketType.PROFILE);
                if (!res) {
                    throw new AmazonS3Exception("Error in saving product image");
                }

                }
            } catch (AmazonS3Exception e ) {
                throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Error in saving product");
            }

        return dbUser;
    }

    @Override
    public Boolean deleteUser(Integer id) {
        try{
            UserDtls user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return false;
            }

            if (user.getProfileImage() != null) {
                fileService.deleteFileS3(user.getProfileImage(), BucketType.PROFILE);
            }

            userRepository.delete(user);
            return true;
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("Amazon S3 error: " + e.getErrorMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error in deleting product");
        }
    }

    @Override
    public Page<UserDtls> getAllUsersPagination(String role, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return userRepository.findAllByRole(role, pageable);
    }

    @Override
    public Page<UserDtls> getAllSearchUsersPagination(String role, String keyword, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

//        Page<UserDtls> res = userRepository.findAllByRoleAndEmailContainingOrNameContainingOrMobileNumberContaining(role, keyword, keyword, keyword, pageable);
        Page<UserDtls> res = userRepository.findAllByRoleAnd(
                role,
                keyword,
                pageable
        );

        if (res.isEmpty()) {
            return null;
        }

        return res;

    }


}
