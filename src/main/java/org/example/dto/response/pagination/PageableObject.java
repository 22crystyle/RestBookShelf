package org.example.dto.response.pagination;

public record PageableObject(
        int pageNumber,
        int pageSize,
        SortObject sort,
        int offset,
        boolean paged,
        boolean unpaged
) {
}
