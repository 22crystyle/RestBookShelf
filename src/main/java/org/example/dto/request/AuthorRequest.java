package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(
        @NotBlank
        String name,
        Integer birth_year
) {
}
