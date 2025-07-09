package org.example.dto.mapper;

import org.example.dto.request.BookRequest;
import org.example.dto.response.BookResponse;
import org.example.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book requestToEntity(BookRequest request);

    BookResponse entityToResponse(Book book);
}
