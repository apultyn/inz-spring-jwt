package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBookRequest {
    @Size(max = 255, message = "Title may not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author may not exceed 255 characters")
    private String author;
}