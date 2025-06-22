package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email may not be blank")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email length must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6-100 characters")
    private String password;

    @NotBlank(message = "Password must be confirmed")
    private String confirmPassword;
}
