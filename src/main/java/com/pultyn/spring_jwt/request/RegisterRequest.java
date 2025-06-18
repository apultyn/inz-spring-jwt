package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "email may not be blank")
    @Email(message = "invalid email format")
    @Size(max = 255, message = "email length must not exceed 255 characters")
    private String email;

    @NotBlank(message = "password may not be blank")
    @Size(min = 6, max = 100, message = "password must be between 6-100 characters")
    private String password;
}
