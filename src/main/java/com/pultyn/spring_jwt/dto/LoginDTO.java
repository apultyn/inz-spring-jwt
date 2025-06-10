package com.pultyn.spring_jwt.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
