package org.example.service;

import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.entity.Author;
import org.example.exception.AuthorNotFound;
import org.example.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository repository;

    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorService service;

    @Test
    void createAuthor_validRequest_returnsEntity() {
        AuthorRequest request = new AuthorRequest("Author", 1900);
        Author mapped = new Author(null, "Author", 1900);
        Author saved = new Author(1L, "Author", 1900);

        when(mapper.requestToEntity(request)).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(saved);

        Author result = service.createAuthor(request);

        assertEquals(1L, result.getId());
        assertEquals("Author", result.getName());
        assertEquals(1900, result.getBirthYear());

        InOrder inOrder = inOrder(mapper, repository);
        inOrder.verify(mapper).requestToEntity(request);
        inOrder.verify(repository).save(mapped);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getAllAuthors_validPageRequest_returnsPage() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Author author = new Author(1L, "Author", 1900);
        Page<Author> expected = new PageImpl<>(Collections.singletonList(author));

        when(repository.findAll(pageRequest)).thenReturn(expected);

        Page<Author> result = service.getPage(pageRequest);

        assertEquals(expected.getTotalElements(), result.getTotalElements());
        assertIterableEquals(expected.getContent(), result.getContent());

        verify(repository).findAll(pageRequest);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getById_existingId_returnsEntity() {
        Long id = 1L;
        Author expected = new Author(id, "Author", 1900);
        when(repository.findById(id)).thenReturn(Optional.of(expected));

        Author result = service.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Author", result.getName());
        assertEquals(1900, result.getBirthYear());

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getById_nonExistingId_throwsAuthorNotFound() {
        Long invalidId = 42L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        AuthorNotFound ex = assertThrows(
                AuthorNotFound.class,
                () -> service.getById(invalidId)
        );
        assertEquals(invalidId, ex.getEntityId());

        verify(repository).findById(invalidId);
        verifyNoMoreInteractions(repository);
    }
}
