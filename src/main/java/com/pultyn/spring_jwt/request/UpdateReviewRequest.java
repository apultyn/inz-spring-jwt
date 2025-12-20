package com.pultyn.spring_jwt.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest {
    @Min(value = 0, message = "Stars value must be between 0 and 5")
    @Max(value = 5, message = "Stars value must be between 0 and 5")
    private Integer stars;

    @Size(max = 2000, message = "Comment may not exceed 2000 characters")
    private String comment;
}
