package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.repository.BookRepository;
import com.pultyn.spring_jwt.request.CreateBookRequest;
import com.pultyn.spring_jwt.request.UpdateBookRequest;
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

    public BookDTO createBook(CreateBookRequest bookRequest) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .reviews(new ArrayList<>())
                .build();

        return new BookDTO(bookRepository.save(book));
    }

    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        bookRepository.delete(book);
    }

    public BookDTO updateBook(Long bookId, UpdateBookRequest bookRequest) {
        Book bookToUpdate = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        bookToUpdate.setAuthor(bookRequest.getAuthor());
        bookToUpdate.setTitle(bookRequest.getTitle());

        bookRepository.save(bookToUpdate);

        return new BookDTO(bookToUpdate);
    }
}
