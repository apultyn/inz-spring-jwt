package com.pultyn.spring_jwt.service;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Set<BookDTO> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookDTO::new)
                .collect(Collectors.toSet());
    }
}
