package com.pultyn.spring_jwt.controller;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.request.ReviewRequest;
import com.pultyn.spring_jwt.response.RegisterResponse;
import com.pultyn.spring_jwt.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<?> getBookReviews(@RequestParam Long bookId) {
        return ResponseEntity.ok(reviewService.getBookReviews(bookId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewDTO review = reviewService.createReview(reviewRequest);
        return new ResponseEntity<ReviewDTO>(review, HttpStatus.CREATED);
    }
}
