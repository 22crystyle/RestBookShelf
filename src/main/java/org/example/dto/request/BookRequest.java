package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @Size(message = "Слишком короткое название книги (мин. 3)", min = 3)
        String title,
        @NotNull(message = "У книги должен быть автор")
        Long authorId,
        @NotNull(message = "Книга должна иметь год выпуска")
        Integer publishedYear,
        @NotBlank(message = "У книги должен быть жанр")
        String genre
) {
}
