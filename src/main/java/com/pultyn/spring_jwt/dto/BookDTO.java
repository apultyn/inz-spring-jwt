package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Book;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private List<ReviewDTO> reviews;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.reviews = book.getReviews().stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }
}
