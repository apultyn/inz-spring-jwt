package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBookRequest {
    @NotBlank(message = "title required")
    @Size(max = 255, message = "title may not exceed 255 characters")
    private String title;

    @NotBlank(message = "author required")
    @Size(max = 255, message = "author may not exceed 255 characters")
    private String author;
}