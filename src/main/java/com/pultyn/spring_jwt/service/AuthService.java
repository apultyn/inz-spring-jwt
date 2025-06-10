package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.LoginDTO;
import com.pultyn.spring_jwt.dto.RegisterDTO;
import com.pultyn.spring_jwt.model.Role;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.RoleRepository;
import com.pultyn.spring_jwt.repository.UserRepository;
import com.pultyn.spring_jwt.response.LoginResponse;
import com.pultyn.spring_jwt.response.RegisterResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("No default user found"));

        UserEntity user = UserEntity.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .roles(Collections.singletonList(role))
                .build();
        userRepository.save(user);
        return new RegisterResponse(String.format("%s registered!", user.getEmail()));
    }

    public LoginResponse login(LoginDTO loginDTO) {
        return new LoginResponse("Logged in");
    }
}
