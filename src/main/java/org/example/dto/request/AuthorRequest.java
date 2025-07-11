package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 3, message = "Слишком короткое имя автора")
        @Pattern(regexp = "^[^0-9]*$", message = "Имя автора не должно содержать цифр")
        String name,
        Integer birthYear
) {
}
