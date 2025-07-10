package org.example.service;

import org.example.dto.mapper.AuthorMapper;
import org.example.dto.request.AuthorRequest;
import org.example.entity.Author;
import org.example.exception.AuthorNotFoundException;
import org.example.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepository repository, AuthorMapper authorMapper) {
        this.repository = repository;
        this.authorMapper = authorMapper;
    }

    @Transactional
    public Author create(AuthorRequest request) {
        Author author = authorMapper.requestToEntity(request);
        return repository.save(author);
    }

    @Transactional(readOnly = true)
    public Page<Author> getPage(PageRequest request) {
        return repository.findAll(request);
    }

    @Transactional(readOnly = true)
    public Author getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }
}
