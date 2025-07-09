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
        BookRequest request = new BookRequest("Title", 1L, 1900, "Unknown");
        Book mapped = new Book(null, "Title", null, 1900, "Unknown");
        Author author = new Author(1L, "Author", 1900);

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
        BookRequest request = new BookRequest("Title", 42L, 1900, "Unknown");
        Book mapped = new Book(null, "Title", null, 1900, "Unknown");

        when(bookMapper.requestToEntity(request)).thenReturn(mapped);
        when(authorRepository.findById(42L)).thenReturn(Optional.empty());

        AuthorNotFound ex = assertThrows(
                AuthorNotFound.class,
                () -> bookService.create(request)
        );
        assertEquals(42L, ex.getEntityId());

        verify(bookMapper).requestToEntity(request);
        verify(authorRepository).findById(42L);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void whenGetList_thenReturnList() {
        Author author = new Author(1L, "Author", 1900);
        List<Book> expected = List.of(
                new Book(1L, "Title", author, 1900, "Unknown"),
                new Book(2L, "Book", author, 2006, "Unknown"),
                new Book(3L, "Who", author, 2077, "Sci-FI")
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
        Author author = new Author(1L, "Author", 1900);
        Book expected = new Book(1L, "Title", author, 1900, "Unknown");

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
        assertEquals(42L, ex.getEntityId());

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void update_ValidUpdateRequest_ReturnsEntity() {
        Long bookId = 1L;
        Long oldAuthorId = 1L;
        Long newAuthorId = 2L;

        Author oldAuthor = new Author(oldAuthorId, "Old Author", 1900);
        Book existing = new Book(bookId, "Old Title", oldAuthor, 1800, "Old Genre");

        Author newAuthor = new Author(newAuthorId, "New Author", 2050);
        BookUpdateRequest request = new BookUpdateRequest("New Title", newAuthor.getId(), 2021, "New Genre");
        Book updated = new Book(bookId, "New Title", newAuthor, 2021, "New Genre");

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
