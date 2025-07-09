package org.example;

import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository repository;

    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorService service;

    @Test
    public void whenValidRequest_whenReturnEntity() {
        AuthorRequest request = new AuthorRequest("Author", 1900);
        Author mapped = new Author();
        mapped.setName("Author");
        mapped.setBirthYear(1900);
        Author saved = new Author(1L, "Author", 1900);

        when(mapper.requestToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(saved);

        Author result = service.createAuthor(request);

        assertNotNull(result);
        assertEquals(saved, result);

        verify(mapper).requestToEntity(request);
        verify(repository).save(mapped);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    public void whenPageRequest_thenGetAuthorPagination() {

    }
}
