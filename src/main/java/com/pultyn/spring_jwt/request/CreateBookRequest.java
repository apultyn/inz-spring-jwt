package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateBookRequest {
    @NotBlank(message = "Title required")
    @Size(max = 255, message = "Title may not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author required")
    @Size(max = 255, message = "Author may not exceed 255 characters")
    private String author;
}
