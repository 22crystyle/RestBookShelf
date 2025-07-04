package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.dto.response.AuthorResponse;
import org.example.dto.response.pagination.PageAuthorResponse;
import org.example.entity.Author;
import org.example.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Author",
        description = "Api для управления авторами"
)
@RestController
@RequestMapping("/api/v1/authors")
@Validated
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @Operation(
            summary = "Создать автора",
            description = "Создает нового автора",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Автор успешно создан",
                            content = @Content(schema = @Schema(implementation = AuthorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody @Valid AuthorRequest request) {
        Author entity = authorService.createAuthor(request);
        AuthorResponse dto = authorMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Получить список авторов",
            description = "Возвращает страницу авторов с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные авторов получены",
                            content = @Content(schema = @Schema(implementation = PageAuthorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса",
                            content = @Content
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getPageOfAuthors(
            @Parameter(description = "Номер страницы начинается с 1", required = true, example = "1")
            @RequestParam @Min(1) int page,
            @Parameter(description = "Размер страницы", required = true, example = "10")
            @RequestParam @Min(1) int size
    ) {
        Page<Author> entities = authorService.getPage(PageRequest.of(page - 1, size));
        Page<AuthorResponse> dtos = entities.map(authorMapper::entityToResponse);
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Получить автора по ID",
            description = "Возвращает данные автора по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Автор найден",
                            content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Автор не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(description = "Идентификатор книги", required = true, example = "1")
            @PathVariable Long id
    ) {
        Author entity = authorService.getById(id);
        AuthorResponse dto = authorMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }
}
