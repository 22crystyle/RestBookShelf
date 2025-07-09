package org.example.dto;

import jakarta.validation.constraints.NotNull;

public class AuthorDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    private Integer birth_year;
}
