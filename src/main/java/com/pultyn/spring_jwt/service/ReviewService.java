package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.model.Review;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.ReviewRepository;
import com.pultyn.spring_jwt.request.ReviewRequest;
import com.pultyn.spring_jwt.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public ReviewDTO createReview(ReviewRequest reviewRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();

        UserEntity user = userService.findUserById(userId);
        Book book = bookService.findBookById(reviewRequest.getBookId());

        Review review = Review.builder()
                .book(book)
                .user(user)
                .stars(reviewRequest.getStars())
                .comment(reviewRequest.getComment())
                .build();

        return new ReviewDTO(reviewRepository.save(review));
    }
}
