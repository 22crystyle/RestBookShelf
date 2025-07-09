package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.dto.response.AuthorResponse;
import org.example.entity.Author;
import org.example.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody @Valid AuthorRequest request) {
        Author entity = authorService.createAuthor(request);
        AuthorResponse dto = authorMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @RequestParam @Min(1) int page,
            @RequestParam @Min(1) int size
    ) {
        Page<Author> entities = authorService.getAllAuthors(PageRequest.of(page - 1, size));
        Page<AuthorResponse> dtos = entities.map(authorMapper::entityToResponse);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        Author entity = authorService.getById(id);
        AuthorResponse dto = authorMapper.entityToResponse(entity);
        return ResponseEntity.ok(dto);
    }
}
