package org.example.dto.mapper;

import org.example.dto.BookDto;
import org.example.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book bookDtoToBook(BookDto dto);
}
