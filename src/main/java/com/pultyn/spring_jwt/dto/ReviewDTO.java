package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private int stars;
    private String comment;
    private String userEmail;

    public ReviewDTO(Review review) {
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.userEmail = review.getUser().getEmail();
    }
}
