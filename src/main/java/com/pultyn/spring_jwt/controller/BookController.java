package com.pultyn.spring_jwt.controller;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.request.CreateBookRequest;
import com.pultyn.spring_jwt.request.UpdateBookRequest;
import com.pultyn.spring_jwt.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("")
    public ResponseEntity<?> getBooks() {
        Set<BookDTO> books = bookService.getBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@RequestBody CreateBookRequest bookRequest) {
        BookDTO book = bookService.createBook(bookRequest);
        return new ResponseEntity<BookDTO>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long bookId,
            @RequestBody UpdateBookRequest bookRequest
    ) throws NotFoundException {
        BookDTO updatedBook = bookService.updateBook(bookId, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) throws NotFoundException {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
