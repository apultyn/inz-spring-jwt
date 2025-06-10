package com.pultyn.spring_jwt.response;

import com.pultyn.spring_jwt.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String msg;
}
