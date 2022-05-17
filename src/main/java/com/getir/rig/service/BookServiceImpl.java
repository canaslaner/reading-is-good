package com.getir.rig.service;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.converter.BookConverter;
import com.getir.rig.exception.RecordNotFoundException;
import com.getir.rig.model.Book;
import com.getir.rig.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Book createBook(final BookDto bookDto) {
        Validate.isTrue(!bookRepository.existsByIsbn(bookDto.getIsbn()), "exception.isbnAlreadyUsed");
        return bookRepository.save(BookConverter.toEntity(bookDto));
    }

    @Override
    public Book save(final Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(final long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Page<Book> findAll(final Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Book updateStock(final long id, final long newStock) {
        final var foundBook = bookRepository.findById(id)
                .map(book -> book.setStock(newStock))
                .orElseThrow(() -> new RecordNotFoundException("exception.bookNotFound"));
        return bookRepository.save(foundBook);
    }
}