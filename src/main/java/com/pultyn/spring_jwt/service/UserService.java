package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserById(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
