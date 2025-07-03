package org.example.dto.response.pagination;

import org.example.dto.response.BookResponse;

import java.util.List;

public record PageBookResponse(
        List<BookResponse> content,
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
