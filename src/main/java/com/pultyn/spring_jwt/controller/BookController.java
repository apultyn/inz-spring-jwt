package com.pultyn.spring_jwt.controller;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
