package com.getir.rig.service;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    Book createBook(BookDto bookDto);

    Book save(Book book);

    Optional<Book> findById(long id);

    Page<Book> findAll(Pageable pageable);

    Book updateStock(long id, long newStock);
}
