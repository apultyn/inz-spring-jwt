package com.pultyn.spring_jwt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReviewRequest {
    private Long bookId;
    private int stars;
    private String comment;
}
