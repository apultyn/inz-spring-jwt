package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private Set<ReviewDTO> reviews;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.reviews = book.getReviews().stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toSet());
    }
}
