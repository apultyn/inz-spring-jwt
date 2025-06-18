package com.pultyn.spring_jwt.repository;

import com.pultyn.spring_jwt.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "ORDER BY b.title ASC")
    List<Book> searchBooks(@Param("searchString") String searchString);
}
