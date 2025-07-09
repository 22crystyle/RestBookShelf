package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookUpdateRequest(
        @NotBlank
        String title,
        @NotNull
        Long authorId,
        @NotNull
        Integer publishedYear,
        @NotBlank
        String genre
) {
}
