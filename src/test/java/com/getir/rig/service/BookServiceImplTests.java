package com.getir.rig.service;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.exception.RecordNotFoundException;
import com.getir.rig.model.Book;
import com.getir.rig.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTests {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    void testCreateBook_whenIsbnAlreadyExists_thenShouldThrowException() {
        // given
        final var dto = BookDto.builder().isbn("isbn").build();
        Mockito.doReturn(true).when(bookRepository).existsByIsbn("isbn");

        // when
        final var exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.createBook(dto));

        // then
        assertEquals("exception.isbnAlreadyUsed", exception.getMessage());
    }

    @Test
    void testCreateBook_whenHappyPath_thenSaveBookSuccessfully() {
        // given
        final var dto = BookDto.builder().isbn("isbn").stock(5L).build();
        final var book = Book.builder().id(1L).build();

        Mockito.doReturn(false).when(bookRepository).existsByIsbn("isbn");
        Mockito.doReturn(book).when(bookRepository).save(ArgumentMatchers.any());

        // when
        final var savedBook = bookService.createBook(dto);

        // then
        assertEquals(book, savedBook);
    }

    @Test
    void testSave_whenErrorOccurred_thenShouldThrowException() {
        // given
        final var book = Book.builder().build();
        Mockito.doThrow(new RuntimeException("test_exception")).when(bookRepository).save(book);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> bookService.save(book));

        // then
        assertEquals("test_exception", exception.getMessage());
    }

    @Test
    void testSave_whenHappyPath_thenSaveObject() {
        // given
        final var book = Book.builder().build();
        Mockito.doReturn(book).when(bookRepository).save(book);

        // when
        final var savedBook = bookService.save(book);

        // then
        assertEquals(book, savedBook);
    }

    @Test
    void testFindById_whenErrorOccurred_thenShouldThrowException() {
        // given
        Mockito.doThrow(new RuntimeException("test_exception")).when(bookRepository).findById(10L);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> bookService.findById(10L));

        // then
        assertEquals("test_exception", exception.getMessage());
    }

    @Test
    void testFindById_whenHappyPath_thenReturnRecord() {
        // given
        final var book = Book.builder().build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById(10L);

        // when
        final var foundBook = bookService.findById(10L);

        // then
        assertTrue(foundBook.isPresent());
        assertEquals(foundBook.get(), book);
    }

    @Test
    void testFindAll_whenErrorOccurred_thenShouldThrowException() {
        // given
        final var pageable = PageRequest.of(1, 2);
        Mockito.doThrow(new RuntimeException("test_exception")).when(bookRepository).findAll(pageable);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> bookService.findAll(pageable));

        // then
        assertEquals("test_exception", exception.getMessage());
    }

    @Test
    void testFindAll_whenHappyPath_thenReturnRecord() {
        // given
        final var pageable = PageRequest.of(1, 2);
        final var mockReturnPage = Mockito.mock(Page.class);
        Mockito.doReturn(mockReturnPage).when(bookRepository).findAll(pageable);

        // when
        final var returnedPage = bookService.findAll(pageable);

        // then
        assertEquals(returnedPage, mockReturnPage);
    }

    @Test
    void testUpdateStock_whenRecordNotFound_thenShouldThrowException() {
        // given
        final var id = 1L;
        final var newStock = 5;
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById(id);

        // when
        final var exception = assertThrows(RecordNotFoundException.class,
                () -> bookService.updateStock(id, newStock));

        // then
        assertEquals("exception.bookNotFound", exception.getMessage());
    }

    @Test
    void testUpdateStock_whenErrorOccurredOnSave_thenShouldThrowException() {
        // given
        final var id = 1L;
        final var newStock = 5;
        Mockito.doThrow(new RuntimeException("test_exception")).when(bookRepository).findById(id);

        // when
        final var exception = assertThrows(RuntimeException.class,
                () -> bookService.updateStock(id, newStock));

        // then
        assertEquals("test_exception", exception.getMessage());
    }

    @Test
    void testUpdateStock_whenHappyPath_thenShouldUpdateStockInfo() {
        // given
        final var id = 1L;
        final var newStock = 5;
        final var mockBook = Book.builder().id(id).stock(0).build();
        Mockito.doReturn(Optional.of(mockBook)).when(bookRepository).findById(id);
        Mockito.doReturn(mockBook).when(bookRepository).save(mockBook);

        // when
        final var savedBook = bookService.updateStock(id, newStock);

        // then
        assertEquals(mockBook, savedBook);
        assertEquals(newStock, savedBook.getStock());
    }
}
