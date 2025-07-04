package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookResponse(
        Long id,
        String title,
        AuthorResponse author,
        @JsonProperty("year")
        Integer publishedYear,
        String genre
) {
}
