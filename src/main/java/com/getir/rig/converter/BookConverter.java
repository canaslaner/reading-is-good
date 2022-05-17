package com.getir.rig.converter;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.model.Book;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;

@UtilityClass
public class BookConverter {

    public static BookDto toDto(final Book book) {
        if (book == null) {
            return null;
        }

        return BookDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .sku(book.getSku())
                .name(book.getName())
                .author(book.getAuthor())
                .description(book.getDescription())
                .stock(book.getStock())
                .price(book.getPrice())
                .active(book.isActive())
                .build();
    }

    public static Book toEntity(final BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        return Book.builder()
                .isbn(bookDto.getIsbn())
                .sku(bookDto.getSku())
                .name(bookDto.getName())
                .author(bookDto.getAuthor())
                .description(bookDto.getDescription())
                .stock(bookDto.getStock())
                .price(bookDto.getPrice())
                .active(BooleanUtils.toBoolean(bookDto.getActive()))
                .build();
    }
}
