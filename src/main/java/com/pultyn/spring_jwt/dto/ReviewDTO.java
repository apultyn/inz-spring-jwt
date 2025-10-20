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
    private Long book_id;
    private int stars;
    private String comment;
    private String user_email;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.book_id = review.getBook().getId();
        this.stars = review.getStars();
        this.comment = review.getComment();
        this.user_email = review.getUser().getEmail();
    }
}
