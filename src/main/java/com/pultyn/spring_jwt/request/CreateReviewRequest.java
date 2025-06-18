package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @NotNull(message = "bookId is required")
    private Long bookId;

    @NotNull(message = "stars value required")
    @Min(value = 0, message = "stars value must be between 0 and 5")
    @Max(value = 5, message = "stars value must be between 0 and 5")
    private Integer stars;

    @NotBlank(message = "comment required")
    @Size(max = 2000, message = "comment may not exceed 2000 characters")
    private String comment;
}
