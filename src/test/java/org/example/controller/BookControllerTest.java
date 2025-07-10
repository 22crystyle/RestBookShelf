package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.mapper.BookMapper;
import org.example.dto.request.BookRequest;
import org.example.dto.request.BookUpdateRequest;
import org.example.dto.response.BookResponse;
import org.example.entity.Book;
import org.example.exception.BookNotFoundException;
import org.example.service.BookService;
import org.example.utils.data.BookData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BookService bookService;
    @MockitoBean
    BookMapper bookMapper;

    @Test
    @DisplayName("POST /books - создать книгу")
    void createBook_validRequest_returns200() throws Exception {
        BookRequest request = BookData.DEFAULT_REQUEST;
        Book entity = BookData.DEFAULT_ENTITY;
        BookResponse response = BookData.DEFAULT_RESPONSE;

        when(bookService.create(request)).thenReturn(entity);
        when(bookMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/books/" + response.id())))
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.author.id").value(response.author().id()))
                .andExpect(jsonPath("$.author.name").value(response.author().name()))
                .andExpect(jsonPath("$.author.birthYear").value(response.author().birthYear()))
                .andExpect(jsonPath("$.year").value(response.publishedYear()))
                .andExpect(jsonPath("$.genre").value(response.genre()))
                .andDo(print());

        verify(bookService).create(request);
        verify(bookMapper).entityToResponse(entity);
        verifyNoMoreInteractions(bookService, bookMapper);
    }

    @Test
    @DisplayName("GET /books/{id} - существующая книга возвращается 200")
    void getBookById_existingBook_returns200AndBody() throws Exception {
        Long id = 1L;
        Book entity = BookData.DEFAULT_ENTITY;
        BookResponse response = BookData.DEFAULT_RESPONSE;

        when(bookService.getById(id)).thenReturn(entity);
        when(bookMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/api/v1/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.genre").value(response.genre()))
                .andExpect(jsonPath("$.year").value(response.publishedYear()))
                .andDo(print());

        verify(bookService).getById(id);
        verify(bookMapper).entityToResponse(entity);
        verifyNoMoreInteractions(bookService, bookMapper);
    }

    @Test
    @DisplayName("PUT /books/{id} - успешное обновление")
    void updateBook_existingBook_returns200AndBody() throws Exception {
        Long id = 1L;
        Book entity = BookData.DEFAULT_ENTITY;
        BookUpdateRequest updateRequest = BookData.updateRequest()
                .withTitle("New Title")
                .withAuthorId(1L)
                .withGenre("New Genre")
                .withPublishedYear(2015)
                .build();
        BookResponse response = BookData.response()
                .withTitle("New Title")
                .withGenre("New Genre")
                .withPublishedYear(2015)
                .build();

        when(bookService.update(id, updateRequest)).thenReturn(entity);
        when(bookMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(put("/api/v1/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.author.id").value(response.author().id()))
                .andExpect(jsonPath("$.author.name").value(response.author().name()))
                .andExpect(jsonPath("$.author.birthYear").value(response.author().birthYear()))
                .andExpect(jsonPath("$.year").value(response.publishedYear()))
                .andExpect(jsonPath("$.genre").value(response.genre()))
                .andDo(print());

        verify(bookService).update(id, updateRequest);
        verify(bookMapper).entityToResponse(entity);
        verifyNoMoreInteractions(bookService, bookMapper);
    }

    @Test
    @DisplayName("DELETE /books/{id} - существующая книга возвращается 204")
    void deleteBook_existing_returns204() throws Exception {
        Long id = 1L;
        when(bookService.deleteById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/books/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(id);
        verifyNoMoreInteractions(bookService, bookMapper);
    }

    @Test
    @DisplayName("GET /books/{id} — 404 при несуществующей книге")
    void getBookById_WhenNotFound_ShouldReturn404() throws Exception {
        Long id = 42L;
        when(bookService.getById(id)).thenThrow(new BookNotFoundException(id));

        mockMvc.perform(get("/api/v1/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }

    @Test
    @DisplayName("POST /books — 400 при отсутствии обязательных полей")
    void createBook_WhenMissingFields_ShouldReturn400() throws Exception {
        String invalidJson = """
                    {
                      "title": "Book",
                      "publishedYear": 1999
                      // no authorId
                    }
                """;

        mockMvc.perform(post("/api/v1/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.message").value(containsString("parse error")));
    }

    @Test
    @DisplayName("DELETE /books/{id} - 404 при несуществующей книге")
    void deleteBook_WhenNotExists_ShouldReturn404() throws Exception {
        Long id = 42L;
        when(bookService.deleteById(id)).thenThrow(new BookNotFoundException(id));

        mockMvc.perform(delete("/api/v1/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }
}
