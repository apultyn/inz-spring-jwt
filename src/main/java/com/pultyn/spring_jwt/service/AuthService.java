package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.LoginDTO;
import com.pultyn.spring_jwt.dto.RegisterDTO;
import com.pultyn.spring_jwt.model.Role;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.RoleRepository;
import com.pultyn.spring_jwt.repository.UserRepository;
import com.pultyn.spring_jwt.response.LoginResponse;
import com.pultyn.spring_jwt.response.RegisterResponse;
import com.pultyn.spring_jwt.security.JwtService;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Transactional
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
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        if (auth.isAuthenticated()) {
            UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new IllegalStateException("User can't be authenticated and not be in database"));
            String jwtToken = jwtService.generateToken(user);
            return new LoginResponse(jwtToken, jwtService.getJwtExpiration());
        } else {
            throw new IllegalArgumentException("Not authenticated");
        }
    }
}
