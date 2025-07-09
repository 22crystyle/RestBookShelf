package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.mapper.BookMapper;
import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.BookResponse;
import org.example.dto.response.pagination.PageBookResponse;
import org.example.entity.Book;
import org.example.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Books",
        description = "API для управления книгами"
)
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

    @Operation(
            summary = "Создать книгу",
            description = "Создает новую книгу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Книга успешно создана",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса",
                            content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        Book entity = bookService.create(request);
        BookResponse dto = bookMapper.entityToResponse(entity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Получить список книг",
            description = "Возвращает список книг",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список книг",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
            }
    )
    @GetMapping
    public ResponseEntity<List<BookResponse>> getListOfBooks() {
        List<Book> entities = bookService.getList();
        List<BookResponse> dtos = entities.stream().map(bookMapper::entityToResponse).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Получить книгу по ID",
            description = "Возвращает книгу по её идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга найдена",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "Идентификатор книги", required = true, example = "1")
            @PathVariable Long id
    ) {
        Book entity = bookService.getById(id);
        BookResponse dto = bookMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Обновить книгу",
            description = "Полностью обновляет информацию о книге",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Книга успешно обновлена",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(description = "Идентификатор книги", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные книги",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookUpdateRequest.class))
            )
            @RequestBody @Valid BookUpdateRequest request
    ) {
        Book entity = bookService.update(id, request);
        BookResponse dto = bookMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Удалить книгу",
            description = "Удаляет книгу по идентификатору",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Книга успешно удалена", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Книга не найдена", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Идентификатор книги", required = true, example = "1")
            @PathVariable Long id
    ) {
        if (bookService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
