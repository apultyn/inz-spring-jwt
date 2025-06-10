package com.pultyn.spring_jwt.controller;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.request.NewBookRequest;
import com.pultyn.spring_jwt.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> createBook(@RequestBody NewBookRequest bookRequest) {
        BookDTO book = bookService.createBook(bookRequest);
        return new ResponseEntity<BookDTO>(book, HttpStatus.CREATED);
    }
}
