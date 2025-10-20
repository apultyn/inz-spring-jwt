package com.pultyn.spring_jwt.response;

import com.pultyn.spring_jwt.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String message;
    private UserDTO user;
}
