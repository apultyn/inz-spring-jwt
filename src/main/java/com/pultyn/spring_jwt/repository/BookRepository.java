package com.pultyn.spring_jwt.repository;

import com.pultyn.spring_jwt.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
