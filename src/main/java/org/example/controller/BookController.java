package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.dto.mapper.BookMapper;
import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.BookResponse;
import org.example.entity.Book;
import org.example.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        Book entity = bookService.add(request);
        BookResponse dto = bookMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam @Valid @Min(1) int page,
            @RequestParam @Valid @Min(1) int size) {
        Page<Book> entities = bookService.getAll(PageRequest.of(page - 1, size));
        Page<BookResponse> dtos = entities.map(bookMapper::entityToResponse);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        Book entity = bookService.getById(id);
        BookResponse dto = bookMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookUpdateRequest request
    ) {
        Book entity = bookService.update(id, request);
        BookResponse dto = bookMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
