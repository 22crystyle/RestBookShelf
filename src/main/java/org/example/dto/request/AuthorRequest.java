package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(
        @NotBlank(message = "Имя автора не должно быть пустым")
        String name,
        Integer birthYear
) {
}
