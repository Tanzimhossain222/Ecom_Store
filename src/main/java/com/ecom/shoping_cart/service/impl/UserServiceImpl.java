package com.ecom.shoping_cart.service.impl;

import com.ecom.shoping_cart.model.UserDtls;
import com.ecom.shoping_cart.repository.UserRepository;
import com.ecom.shoping_cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDtls saveUser(UserDtls userDtls) {
        userDtls.setRole("ROLE_USER");
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
}
