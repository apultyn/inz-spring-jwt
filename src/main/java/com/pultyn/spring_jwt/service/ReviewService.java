package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.exceptions.InvalidDataException;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.model.Review;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.ReviewRepository;
import com.pultyn.spring_jwt.request.CreateReviewRequest;
import com.pultyn.spring_jwt.request.UpdateReviewRequest;
import com.pultyn.spring_jwt.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    public Set<ReviewDTO> getBookReviews(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toSet());
    }

    public ReviewDTO createReview(CreateReviewRequest createReviewRequest)
            throws NotFoundException, InvalidDataException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        UserEntity user = userService.findUserById(userId);
        Book book = bookService.findBookById(createReviewRequest.getBookId());

        Review review = Review.builder()
                .book(book)
                .user(user)
                .stars(createReviewRequest.getStars())
                .comment(createReviewRequest.getComment())
                .build();

        if (review.verifyReview()) {
            return new ReviewDTO(reviewRepository.save(review));
        } else {
            throw new InvalidDataException("Star value must be between 0-5");
        }
    }

    public ReviewDTO updateReview(
            Long reviewId,
            UpdateReviewRequest reviewRequest
    ) throws NotFoundException, InvalidDataException {
        Review reviewToUpdate = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        reviewToUpdate.setStars(reviewToUpdate.getStars());
        reviewToUpdate.setComment(reviewToUpdate.getComment());

        if (reviewToUpdate.verifyReview()) {
            reviewRepository.save(reviewToUpdate);
            return new ReviewDTO(reviewToUpdate);
        } else {
            throw new InvalidDataException("Star value must be between 0-5");
        }
    }

    public void deleteReview(Long reviewId) throws NotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        reviewRepository.delete(review);
    }
}
