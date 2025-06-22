package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long bookId;
    private int stars;
    private String comment;
    private String userEmail;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.bookId = review.getBook().getId();
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.userEmail = review.getUser().getEmail();
    }
}
