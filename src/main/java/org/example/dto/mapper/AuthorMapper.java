package org.example.dto.mapper;

import org.example.dto.request.AuthorRequest;
import org.example.dto.response.AuthorResponse;
import org.example.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author requestToEntity(AuthorRequest request);

    AuthorResponse entityToResponse(Author entity);
}
