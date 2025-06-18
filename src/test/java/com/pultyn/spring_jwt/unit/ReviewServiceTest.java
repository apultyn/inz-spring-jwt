package com.pultyn.spring_jwt.unit;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.model.Review;
import com.pultyn.spring_jwt.model.Role;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.ReviewRepository;
import com.pultyn.spring_jwt.service.BookService;
import com.pultyn.spring_jwt.service.ReviewService;
import com.pultyn.spring_jwt.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @InjectMocks
    private ReviewService reviewService;



    @Test
    void getBookReviews_success() throws NotFoundException {
        final List<Role> userRole = new ArrayList<>(List.of(new Role(1L, "USER")));
        final UserEntity user1 = new UserEntity(1L, "passwd", "user1@example.com", userRole, new ArrayList<>());
        final UserEntity user2 = new UserEntity(2L, "passwd", "user2@example.com", userRole, new ArrayList<>());
        final Book book = new Book(1L, "Title 1", "Author", new ArrayList<>());

        List<Review> reviews = new ArrayList<>(List.of(
                new Review(1L, 5, "Comment 1", user1, book),
                new Review(2L, 3, "Comment 2", user2, book)
        ));
        when(reviewRepository.findByBookId(1L)).thenReturn(reviews);

        List<ReviewDTO> answer = reviewService.getBookReviews(1L);
        assertThat(answer).isEqualTo(reviews.stream().map(ReviewDTO::new).collect(Collectors.toList()));
    }

    @Test
    void getBookReviews_notExistingBook() {
        when(reviewRepository.findByBookId(1L)).thenReturn(new ArrayList<>());
        assertThat(reviewService.getBookReviews(1L)).isEqualTo(new ArrayList<>());
    }
}
