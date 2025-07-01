package org.example.dto.response;

import org.example.entity.Author;

public record BookResponse(
        Long id,
        String title,
        Author author,
        Integer publishedYear,
        String genre
) {
}
