package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BookDto {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    private Long author_id;
    @NotEmpty
    private Integer year;
    @NotBlank
    private String genre;
}