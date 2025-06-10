package com.pultyn.spring_jwt.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRequest {
    private Long book_id;
    private int stars;
    private String comment;
}
