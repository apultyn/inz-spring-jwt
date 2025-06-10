package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BookDTO {
    private String title;
    private String author;

    public BookDTO(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
    }
}
