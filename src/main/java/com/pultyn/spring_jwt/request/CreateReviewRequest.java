package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReviewRequest {
    @NotNull(message = "BookId is required")
    private Long bookId;

    @NotNull(message = "Stars value required")
    @Min(value = 0, message = "Stars value must be between 0 and 5")
    @Max(value = 5, message = "Stars value must be between 0 and 5")
    private Integer stars;

    @NotBlank(message = "Comment required")
    @Size(max = 2000, message = "Comment may not exceed 2000 characters")
    private String comment;
}
