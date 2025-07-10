package org.example.service;

import org.example.dto.mapper.BookMapper;
import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.exception.AuthorNotFoundException;
import org.example.exception.BookNotFoundException;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.utils.data.AuthorData;
import org.example.utils.data.BookData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("create: при валидном запросе создает книгу, и возвращает объект")
    void createBook_whenValidRequest_saveBookAndReturnsEntity() {
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
        assertEquals(1970, result.getPublishedYear());
        assertEquals("Unknown", result.getGenre());

        InOrder inOrder = inOrder(bookMapper, authorRepository, bookRepository);
        inOrder.verify(bookMapper).requestToEntity(request);
        inOrder.verify(authorRepository).findById(1L);
        inOrder.verify(bookRepository).save(mapped);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("create: когда не правильный идентификатор пользователя, выбрасывает AuthorNotFoundException")
    void createBook_whenAuthorMissing_throwsAuthorNotFoundException() {
        Long illegalId = 42L;
        BookRequest request = BookData.request().withAuthorId(42L).build();
        Book mapped = BookData.DEFAULT_ENTITY;

        when(bookMapper.requestToEntity(request)).thenReturn(mapped);
        when(authorRepository.findById(illegalId)).thenReturn(Optional.empty());

        AuthorNotFoundException ex = assertThrows(
                AuthorNotFoundException.class,
                () -> bookService.create(request)
        );
        assertEquals(illegalId, ex.getEntityId());

        verify(bookMapper).requestToEntity(request);
        verify(authorRepository).findById(illegalId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("getList: возвращает список всех книг")
    void getList_returnsList() {
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
    @DisplayName("getById: при успешном запросе возвращает объект")
    void getById_ValidRequest_returnsEntity() {
        Long id = 1L;
        Book expected = BookData.DEFAULT_ENTITY;

        when(bookRepository.findById(id)).thenReturn(Optional.of(expected));
        Book result = bookService.getById(id);

        assertEquals(expected, result);

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("getById: если книги нет в репозитории, бросает BookNotFoundException")
    void getById_whenNotExists_throwsBookNotFoundException() {
        Long id = 42L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(id))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("update: при успешном запросе, возвращает обновленный объект")
    void update_validUpdateRequest_returnsEntity() {
        Long bookId = 1L;
        Long oldAuthorId = 1L;
        Long newAuthorId = 2L;

        Author oldAuthor = AuthorData.entity()
                .withId(oldAuthorId)
                .withName("Old Author")
                .withBirthYear(1970).build();
        Book existing = BookData.entity()
                .withId(bookId)
                .withTitle("Old Title").
                withAuthor(oldAuthor)
                .withPublishedYear(1970)
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

    @Test
    @DisplayName("update: если книги нет, бросает BookNotFounException")
    void update_whenBookNotFound_throwsBookNotFoundException() {
        BookUpdateRequest updateRequest = BookData.DEFAULT_UPDATE_REQUEST;
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(id, updateRequest))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));


        verify(bookRepository).findById(id);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById: удаляет существующую книгу и возвращает true")
    void deleteById_whenExists_deleteAndReturnsTrue() {
        Long id = 42L;
        when(bookRepository.existsById(id)).thenReturn(true);

        boolean result = bookService.deleteById(id);

        assertThat(result).isTrue();
        InOrder inOrder = inOrder(bookRepository);
        inOrder.verify(bookRepository).existsById(id);
        inOrder.verify(bookRepository).deleteById(id);
    }
}
