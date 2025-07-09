package org.example.dto.response.pagination;

import org.example.dto.response.AuthorResponse;

import java.util.List;

public record PageAuthorResponse(
        List<AuthorResponse> content,
        PageableObject pageable,
        boolean last,
        int totalElements,
        int totalPages,
        boolean first,
        int size,
        int number,
        SortObject sort,
        int numberOfElements,
        boolean empty
) {
}
