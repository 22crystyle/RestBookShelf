package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.dto.response.AuthorResponse;
import org.example.entity.Author;
import org.example.service.AuthorService;
import org.example.utils.data.AuthorData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    AuthorService authorService;
    @MockitoBean
    AuthorMapper authorMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/authors — успешное создание")
    void createAuthor_validRequest_returns200() throws Exception {
        AuthorRequest request = AuthorData.DEFAULT_REQUEST;
        Author entity = AuthorData.DEFAULT_ENTITY;
        AuthorResponse response = AuthorData.DEFAULT_RESPONSE;

        when(authorService.createAuthor(request)).thenReturn(entity);
        when(authorMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Author"))
                .andExpect(jsonPath("$.birthYear").value(1970))
                .andDo(print());

        verify(authorService).createAuthor(request);
        verify(authorMapper).entityToResponse(entity);
        verifyNoMoreInteractions(authorService, authorMapper);
    }

    @Test
    @DisplayName("GET /api/v1/authors?page&size — возвращает страницу")
    void getPageOfAuthors_validParams_returnsPage() throws Exception {
        int page = 2;
        int size = 5;
        Author entity = AuthorData.DEFAULT_ENTITY;
        Page<Author> pageEntities = new PageImpl<>(List.of(entity));

        AuthorResponse response = AuthorData.DEFAULT_RESPONSE;
        when(authorService.getPage(PageRequest.of(page - 1, size)))
                .thenReturn(pageEntities);
        when(authorMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/api/v1/authors")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Author"))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andDo(print());

        verify(authorService).getPage(PageRequest.of(page - 1, size));
        verify(authorMapper).entityToResponse(entity);
        verifyNoMoreInteractions(authorService, authorMapper);
    }

    @Test
    @DisplayName("GET /api/v1/authors/{id} — успешный запрос")
    void getAuthorById_existingId_returns200() throws Exception {
        Long id = 1L;
        Author entity = AuthorData.DEFAULT_ENTITY;
        AuthorResponse response = AuthorData.DEFAULT_RESPONSE;

        when(authorService.getById(id)).thenReturn(entity);
        when(authorMapper.entityToResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/api/v1/authors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Author"))
                .andExpect(jsonPath("$.birthYear").value(1970))
                .andDo(print());

        verify(authorService).getById(id);
        verify(authorMapper).entityToResponse(entity);
        verifyNoMoreInteractions(authorService, authorMapper);
    }
}
