package com.pultyn.spring_jwt.controller;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.Review;
import com.pultyn.spring_jwt.request.CreateReviewRequest;
import com.pultyn.spring_jwt.request.UpdateReviewRequest;
import com.pultyn.spring_jwt.service.ReviewService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<?> getBookReviews(@RequestParam Long bookId) {
        return ResponseEntity.ok(reviewService.getBookReviews(bookId));
    }

    @GetMapping("/{reviewId}/")
    public ResponseEntity<?> getReview(@NotNull @PathVariable Long reviewId) throws NotFoundException {
        Review review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(new ReviewDTO(review));
    }


    @PostMapping("/")
    @PreAuthorize("hasRole('BOOK_USER')")
    @Transactional
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest createReviewRequest)
            throws NotFoundException {
        ReviewDTO review = reviewService.createReview(createReviewRequest);
        return new ResponseEntity<ReviewDTO>(review, HttpStatus.CREATED);
    }

    @PatchMapping("/{reviewId}/")
    @PreAuthorize("hasRole('BOOK_ADMIN')")
    @Transactional
    public ResponseEntity<ReviewDTO> updateReview(
            @NotNull @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest reviewRequest
    ) throws NotFoundException {
        ReviewDTO review = reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}/")
    @PreAuthorize("hasRole('BOOK_ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteReview(@NotNull @PathVariable Long reviewId) throws NotFoundException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
