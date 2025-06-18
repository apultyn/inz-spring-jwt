package com.pultyn.spring_jwt.unit;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.repository.BookRepository;
import com.pultyn.spring_jwt.request.CreateBookRequest;
import com.pultyn.spring_jwt.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookService bookService;

    @Test
    void createBook_success() {
        CreateBookRequest req = new CreateBookRequest();
        req.setTitle("Title");
        req.setAuthor("Author");

        Book saved = Book.builder()
                .id(1L)
                .title(req.getTitle())
                .author(req.getAuthor())
                .reviews(new ArrayList<>())
                .build();
        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        BookDTO result = bookService.createBook(req);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Title");
        assertThat(result.getAuthor()).isEqualTo("Author");

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Title");
    }


}
