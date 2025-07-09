package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.entity.Author;

public record BookRequest(
        @NotBlank
        String title,
        @NotNull
        Author author,
        @NotNull
        Integer publishedYear,
        @NotBlank
        String genre
) {
}
