package com.getir.rig.converter;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BookConverterTests {

    @Test
    void testToDto_whenInputNull_thenShouldReturnNull() {
        Assertions.assertNull(BookConverter.toDto(null));
    }

    @Test
    void testToDto_whenHappyPath_thenShouldCreateSuccessfully() {
        // given
        final var book = Book.builder()
                .id(1L)
                .isbn("isbn")
                .sku("sku")
                .name("name")
                .author("author")
                .description("description")
                .stock(1)
                .price(BigDecimal.TEN)
                .active(true)
                .version(5)
                .build();

        // when
        final var dto = BookConverter.toDto(book);

        // then
        Assertions.assertEquals(book.getId(), dto.getId());
        Assertions.assertEquals(book.getIsbn(), dto.getIsbn());
        Assertions.assertEquals(book.getSku(), dto.getSku());
        Assertions.assertEquals(book.getName(), dto.getName());
        Assertions.assertEquals(book.getAuthor(), dto.getAuthor());
        Assertions.assertEquals(book.getDescription(), dto.getDescription());
        Assertions.assertEquals(book.getStock(), dto.getStock());
        Assertions.assertEquals(book.getPrice(), dto.getPrice());
        Assertions.assertEquals(book.isActive(), dto.getActive());
    }

    @Test
    void testToEntity_whenInputNull_thenShouldReturnNull() {
        Assertions.assertNull(BookConverter.toEntity(null));
    }

    @Test
    void testToEntity_whenHappyPath_thenShouldCreateSuccessfully() {
        // given
        final var dto = BookDto.builder()
                .id(1L)
                .isbn("isbn")
                .sku("sku")
                .name("name")
                .author("author")
                .description("description")
                .stock(1L)
                .price(BigDecimal.TEN)
                .active(true)
                .build();

        // when
        final var book = BookConverter.toEntity(dto);

        // then
        Assertions.assertNull(book.getId());
        Assertions.assertEquals(dto.getIsbn(), book.getIsbn());
        Assertions.assertEquals(dto.getSku(), book.getSku());
        Assertions.assertEquals(dto.getName(), book.getName());
        Assertions.assertEquals(dto.getAuthor(), book.getAuthor());
        Assertions.assertEquals(dto.getDescription(), book.getDescription());
        Assertions.assertEquals(dto.getStock(), book.getStock());
        Assertions.assertEquals(dto.getPrice(), book.getPrice());
        Assertions.assertEquals(dto.getActive(), book.isActive());
    }
}
