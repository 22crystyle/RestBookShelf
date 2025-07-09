package org.example.service;

import org.example.dto.mapper.BookMapper;
import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.exception.AuthorNotFound;
import org.example.exception.BookNotFound;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.utils.data.AuthorData;
import org.example.utils.data.BookData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

    @Test
    void create_ValidBookRequest_ReturnsEntity() {
        BookRequest request = BookData.DEFAULT_REQUEST;
        Book mapped = BookData.entity().withId(null).build();
        Author author = AuthorData.DEFAULT_ENTITY;

        when(bookMapper.requestToEntity(request)).thenReturn(mapped);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(mapped)).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        Book result = bookService.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Title", result.getTitle());
        assertSame(author, result.getAuthor());
        assertEquals(1900, result.getPublishedYear());
        assertEquals("Unknown", result.getGenre());

        InOrder inOrder = inOrder(bookMapper, authorRepository, bookRepository);
        inOrder.verify(bookMapper).requestToEntity(request);
        inOrder.verify(authorRepository).findById(1L);
        inOrder.verify(bookRepository).save(mapped);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void create_throwsAuthorNotFound_whenAuthorMissing() {
        Long illegalId = 42L;
        BookRequest request = BookData.request().withAuthorId(42L).build();
        Book mapped = BookData.DEFAULT_ENTITY;

        when(bookMapper.requestToEntity(request)).thenReturn(mapped);
        when(authorRepository.findById(illegalId)).thenReturn(Optional.empty());

        AuthorNotFound ex = assertThrows(
                AuthorNotFound.class,
                () -> bookService.create(request)
        );
        assertEquals(illegalId, ex.getEntityId());

        verify(bookMapper).requestToEntity(request);
        verify(authorRepository).findById(illegalId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void whenGetList_thenReturnList() {
        Author author = AuthorData.DEFAULT_ENTITY;
        List<Book> expected = List.of(
                BookData.DEFAULT_ENTITY,
                BookData.entity().withId(2L).withTitle("Book").withAuthor(author).withPublishedYear(2006).withGenre("Unknown").build(),
                BookData.entity().withId(3L).withTitle("Who").withAuthor(author).withPublishedYear(2077).withGenre("Sci-Fi").build()
        );

        when(bookRepository.findAll()).thenReturn(expected);
        List<Book> result = bookService.getList();

        assertEquals(expected, result);
        assertIterableEquals(expected, result);

        verify(bookRepository).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getById_ValidRequest_ReturnsEntity() {
        Long id = 1L;
        Book expected = BookData.DEFAULT_ENTITY;

        when(bookRepository.findById(id)).thenReturn(Optional.of(expected));
        Book result = bookService.getById(id);

        assertEquals(expected, result);

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void getById_throwsBookNotFound_whenInvalidId() {
        Long id = 42L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        BookNotFound ex = assertThrows(
                BookNotFound.class,
                () -> bookService.getById(id));
        assertEquals(id, ex.getEntityId());

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_validUpdateRequest_returnsEntity() {
        Long bookId = 1L;
        Long oldAuthorId = 1L;
        Long newAuthorId = 2L;

        Author oldAuthor = AuthorData.entity()
                .withId(oldAuthorId)
                .withName("Old Author")
                .withBirthYear(1900).build();
        Book existing = BookData.entity()
                .withId(bookId)
                .withTitle("Old Title").
                withAuthor(oldAuthor)
                .withPublishedYear(1900)
                .withGenre("Old Genre").build();

        Author newAuthor = AuthorData.entity()
                .withId(newAuthorId)
                .withName("New Author")
                .withBirthYear(2050).build();
        BookUpdateRequest request = BookData.updateRequest()
                .withTitle("New Title")
                .withAuthorId(newAuthorId)
                .withPublishedYear(2021)
                .withGenre("New Genre").build();
        Book updated = BookData.entity()
                .withId(bookId)
                .withTitle("New Title")
                .withAuthor(newAuthor)
                .withPublishedYear(2021)
                .withGenre("New Genre").build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(newAuthor.getId())).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.update(bookId, request);

        assertEquals(updated.getId(), result.getId());
        assertEquals(updated.getTitle(), result.getTitle());
        assertSame(updated.getAuthor(), result.getAuthor());
        assertEquals(updated.getPublishedYear(), result.getPublishedYear());
        assertEquals(updated.getGenre(), result.getGenre());

        InOrder inOrder = inOrder(bookRepository, authorRepository);
        inOrder.verify(bookRepository).findById(bookId);
        inOrder.verify(authorRepository).findById(newAuthorId);
        inOrder.verify(bookRepository).save(existing);
        inOrder.verifyNoMoreInteractions();
    }
}
