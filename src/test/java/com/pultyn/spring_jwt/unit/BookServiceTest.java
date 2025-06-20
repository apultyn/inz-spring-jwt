package com.pultyn.spring_jwt.unit;

import com.pultyn.spring_jwt.dto.BookDTO;
import com.pultyn.spring_jwt.exceptions.NotFoundException;
import com.pultyn.spring_jwt.model.Book;
import com.pultyn.spring_jwt.repository.BookRepository;
import com.pultyn.spring_jwt.request.CreateBookRequest;
import com.pultyn.spring_jwt.request.UpdateBookRequest;
import com.pultyn.spring_jwt.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookService bookService;

    @Test
    void findBookById_success() throws NotFoundException {
        Book toFind = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .reviews(new ArrayList<>())
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(toFind));

        Book found = bookService.findBookById(1L);
        assertThat(found).isEqualTo(toFind);
    }

    @Test
    void findBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findBookById(1L))
                .isInstanceOf(NotFoundException.class)
                .message().isEqualTo("Book not found");
    }

    @Test
    void getBooks_emptyString() {
        List<Book> books = List.of(
                new Book(1L, "Title", "Author", new ArrayList<>()),
                new Book(2L, "Another book", "Different Author", new ArrayList<>()),
                new Book(3L, "The best book", "Different Author", new ArrayList<>())
        );
        when(bookRepository.searchBooks("")).thenReturn(books);

        List<BookDTO> bookDTOS = bookService.getBooks("");
        assertThat(bookDTOS.size()).isEqualTo(3);
    }

    @Test
    void getBooks_searchString() {
        List<Book> books = List.of(
                new Book(1L, "Title", "Author", new ArrayList<>()),
                new Book(3L, "The best book", "Different Author", new ArrayList<>())
        );
        when(bookRepository.searchBooks("Diff")).thenReturn(books);

        List<BookDTO> bookDTOS = bookService.getBooks("Diff");
        assertThat(bookDTOS.size()).isEqualTo(2);
    }

    @Test
    void createBook_success() {
        CreateBookRequest req = new CreateBookRequest("Title", "Author");
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

    @Test
    void createBook_constraintViolation() {
        when(bookRepository.save(any(Book.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> bookService.createBook(new CreateBookRequest("Title", "Author")))
                .isInstanceOf(DataIntegrityViolationException.class)
                .message().isEqualTo("Book must have unique combination of title and author");
    }

    @Test
    void deleteBook_success() throws NotFoundException {
        Book toDelete = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .reviews(new ArrayList<>())
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(toDelete));
        doNothing().when(bookRepository).delete(toDelete);

        bookService.deleteBook(1L);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).delete(captor.capture());
        assertThat(captor.getValue()).isEqualTo(toDelete);
    }

    @Test
    void deleteBook_notFound() throws NotFoundException {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.deleteBook(1L))
                .isInstanceOf(NotFoundException.class)
                .message().isEqualTo("Book not found");
    }

    @Test
    void updateBook_success() throws NotFoundException {
        Book toUpdate = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .reviews(new ArrayList<>())
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        UpdateBookRequest req = new UpdateBookRequest();
        req.setAuthor("Author");
        req.setTitle("Title 2");

        BookDTO result = bookService.updateBook(1L, req);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Title 2");
        assertThat(result.getAuthor()).isEqualTo("Author");

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Title 2");
        assertThat(captor.getValue().getAuthor()).isEqualTo("Author");
    }

    @Test
    void updateBook_notFound() throws NotFoundException {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.updateBook(1L, new UpdateBookRequest()))
                .isInstanceOf(NotFoundException.class)
                .message().isEqualTo("Book not found");
    }
}
