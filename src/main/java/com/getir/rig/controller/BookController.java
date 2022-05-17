package com.getir.rig.controller;

import com.getir.rig.controller.dto.BookDto;
import com.getir.rig.controller.dto.book.CreateBookRequest;
import com.getir.rig.controller.dto.book.GetBookResponse;
import com.getir.rig.controller.dto.book.UpdateBookStockRequest;
import com.getir.rig.controller.dto.book.UpdateBookStockResponse;
import com.getir.rig.converter.BookConverter;
import com.getir.rig.exception.RecordNotFoundException;
import com.getir.rig.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(value = "/v1/book",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Book API", description = "Manages book-related processes")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates new books",
            description = "Creates new books with their metadata",
            security = @SecurityRequirement(name = "Bearer"))
    public void createBook(@Valid @RequestBody final CreateBookRequest createBookRequest) {
        bookService.createBook(createBookRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds book by id",
            description = "Returns book by its id information",
            security = @SecurityRequirement(name = "Bearer"))
    public GetBookResponse getBook(@PathVariable final long id) {
        final var book = bookService.findById(id)
                .map(BookConverter::toDto)
                .orElseThrow(() -> new RecordNotFoundException("exception.bookNotFound"));
        return GetBookResponse.builder().book(book).build();
    }

    @GetMapping(consumes = MediaType.ALL_VALUE)
    @Operation(summary = "Finds all books",
            description = "Finds all books by page with query parameters",
            security = @SecurityRequirement(name = "Bearer"))
    public Page<BookDto> getAllBooks(@ParameterObject @NotNull @Valid final Pageable pageable) {
        return bookService.findAll(pageable).map(BookConverter::toDto);
    }

    @PostMapping("/{id}/stock")
    @Operation(summary = "Updates specific book's stock",
            description = "Updates single specific book's stock information",
            security = @SecurityRequirement(name = "Bearer"))
    public UpdateBookStockResponse updateBookStock(@PathVariable final long id,
                                                   @RequestBody @NotNull @Valid final UpdateBookStockRequest request) {
        final var book = bookService.updateStock(id, request.getStock());
        return UpdateBookStockResponse.builder().book(BookConverter.toDto(book)).build();
    }
}
