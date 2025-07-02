package org.example.dto.response;

public record AuthorResponse(
        Long id,
        String name,
        Integer birthYear
) {

}
