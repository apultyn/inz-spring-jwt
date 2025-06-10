package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.repository.BookRepository;
import com.pultyn.spring_jwt.request.NewBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    public Set<BookDTO> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookDTO::new)
                .collect(Collectors.toSet());
    }

    public BookDTO createBook(NewBookRequest bookRequest) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .reviews(new ArrayList<>())
                .build();

        return new BookDTO(bookRepository.save(book));
    }
}
