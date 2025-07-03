package org.example.dto.response.pagination;

public record SortObject(
        boolean sorted,
        boolean empty,
        boolean unsorted
) {
}
