package com.pultyn.spring_jwt.unit;

import com.pultyn.spring_jwt.dto.ReviewDTO;
import com.pultyn.spring_jwt.enums.UserRole;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.model.Review;
import com.pultyn.spring_jwt.model.UserEntity;
import com.pultyn.spring_jwt.repository.ReviewRepository;
import com.pultyn.spring_jwt.request.CreateReviewRequest;
import com.pultyn.spring_jwt.request.UpdateReviewRequest;
import com.pultyn.spring_jwt.security.CustomUserDetails;
import com.pultyn.spring_jwt.service.BookService;
import com.pultyn.spring_jwt.service.ReviewService;
import com.pultyn.spring_jwt.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @Mock
    private Authentication auth;
    @Mock
    private CustomUserDetails principal;
    @InjectMocks
    private ReviewService reviewService;


    @Test
    void getBookReviews_success() {
        final UserEntity user1 = new UserEntity(1L, "passwd", "user1@example.com", UserRole.USER, new ArrayList<>());
        final UserEntity user2 = new UserEntity(2L, "passwd", "user2@example.com", UserRole.USER, new ArrayList<>());
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

    @Test
    void updateReview_success() throws NotFoundException {
        final UserEntity user = new UserEntity(1L, "passwd", "user1@example.com", UserRole.USER, new ArrayList<>());
        final Book book = new Book(1L, "Title 1", "Author", new ArrayList<>());
        Review review = new Review(1L, 5, "Comment 1", user, book);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review(1L, 3, "Updated comment", user, book));

        UpdateReviewRequest req = new UpdateReviewRequest(3, "Updated comment");
        ReviewDTO result = reviewService.updateReview(1L, req);

        assertThat(result).isEqualTo(new ReviewDTO(1L, 1L, 3, "Updated comment", "user1@example.com"));

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());
        assertThat(captor.getValue().getBook()).isEqualTo(book);
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getStars()).isEqualTo(3);
        assertThat(captor.getValue().getComment()).isEqualTo("Updated comment");
    }

    @Test
    void updateReview_reviewNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.updateReview(1L, new UpdateReviewRequest()))
                .isInstanceOf(NotFoundException.class)
                .message().isEqualTo("Review not found");
    }

    @Test
    void deleteReview_success() throws NotFoundException {
        final UserEntity user = new UserEntity(1L, "passwd", "user1@example.com", UserRole.USER, new ArrayList<>());
        final Book book = new Book(1L, "Title 1", "Author", new ArrayList<>());
        Review review = new Review(1L, 5, "Comment 1", user, book);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).delete(captor.capture());
        assertThat(captor.getValue()).isEqualTo(review);
    }

    @Test
    void deleteReview_reviewNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.deleteReview(1L))
                .isInstanceOf(NotFoundException.class)
                .message().isEqualTo("Review not found");
    }

    @Nested
    class WithAuthenticatedUser {
        @BeforeEach
        void initSecurity() {
            when(principal.getId()).thenReturn(1L);
            when(auth.getPrincipal()).thenReturn(principal);

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(auth);
            SecurityContextHolder.setContext(securityContext);
        }

        @AfterEach
        void clearSecurity() {
            SecurityContextHolder.clearContext();
        }

        @Test
        void createReview_success() throws NotFoundException {
            UserEntity user = new UserEntity(1L, "passwd", "user1@example.com", UserRole.USER, new ArrayList<>());
            when(userService.findUserById(1L)).thenReturn(user);

            Book book = new Book(1L, "Title 1", "Author", new ArrayList<>());
            when(bookService.findBookById(1L)).thenReturn(book);

            Review review = new Review(1L, 5, "Fine book", user, book);
            when(reviewRepository.save(any(Review.class))).thenReturn(review);

            CreateReviewRequest req = new CreateReviewRequest(1L, 5, "Fine book");

            ReviewDTO result = reviewService.createReview(req);

            assertThat(result).isEqualTo(new ReviewDTO(1L, 1L, 5, "Fine book", "user1@example.com"));

            ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
            verify(reviewRepository).save(captor.capture());
            assertThat(captor.getValue().getBook()).isEqualTo(book);
            assertThat(captor.getValue().getUser()).isEqualTo(user);
            assertThat(captor.getValue().getStars()).isEqualTo(5);
            assertThat(captor.getValue().getComment()).isEqualTo("Fine book");
        }

        @Test
        void createReview_userNotExist() throws NotFoundException {
            when(userService.findUserById(1L)).thenThrow(NotFoundException.class);

            CreateReviewRequest req = new CreateReviewRequest(1L, 5, "Fine book");
            assertThatThrownBy(() -> reviewService.createReview(req))
                    .isInstanceOf(IllegalStateException.class)
                    .message().isEqualTo("User with ID from auth does not exist in database");
        }

        @Test
        void createReview_bookNotExist() throws NotFoundException {
            when(bookService.findBookById(1L)).thenThrow(new NotFoundException("Book not found"));
            when(userService.findUserById(1L)).thenReturn(new UserEntity());

            CreateReviewRequest req = new CreateReviewRequest(1L, 5, "Fine book");
            assertThatThrownBy(() -> reviewService.createReview(req))
                    .isInstanceOf(NotFoundException.class)
                    .message().isEqualTo("Book not found");
        }

        @Test
        void createReview_duplicateReview() throws NotFoundException {
            final UserEntity user = new UserEntity(1L, "passwd", "user1@example.com", UserRole.USER, new ArrayList<>());
            when(userService.findUserById(1L)).thenReturn(user);

            final Book book = new Book(1L, "Title 1", "Author", new ArrayList<>());
            when(bookService.findBookById(1L)).thenReturn(book);

            when(reviewRepository.save(any(Review.class))).thenThrow(DataIntegrityViolationException.class);

            CreateReviewRequest req = new CreateReviewRequest(1L, 3, "Comment");

            assertThatThrownBy(() -> reviewService.createReview(req))
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .message().isEqualTo("User can write max 1 review per book");
        }
    }
}
