package org.example.dto.mapper;

import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author authorDtoToAuthor(AuthorDto dto);
}
