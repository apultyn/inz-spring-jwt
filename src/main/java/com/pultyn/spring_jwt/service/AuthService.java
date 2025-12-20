package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.UserDTO;
import com.pultyn.spring_jwt.enums.UserRole;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.UserRepository;
import com.pultyn.spring_jwt.request.LoginRequest;
import com.pultyn.spring_jwt.request.RegisterRequest;
import com.pultyn.spring_jwt.response.LoginResponse;
import com.pultyn.spring_jwt.response.RegisterResponse;
import com.pultyn.spring_jwt.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirm_password())) {
            throw new IllegalArgumentException("Password mismatch");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }

        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(Set.of(UserRole.BOOK_USER))
                .reviews(new ArrayList<>())
                .build();
        userRepository.save(user);
        return new RegisterResponse(String.format("%s registered!", user.getEmail()), new UserDTO(user));
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("User was authenticated but not found in database"));
        String jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken, jwtService.getJwtExpiration());
    }
}
