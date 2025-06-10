package com.pultyn.spring_jwt.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
