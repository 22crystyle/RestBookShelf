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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper mapper, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Book add(BookRequest request) {
        Book book = mapper.requestToEntity(request);
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new AuthorNotFound(request.authorId()));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Page<Book> getAll(PageRequest pageRequest) {
        return bookRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFound(id));
    }

    @Transactional
    public Book update(Long id, BookUpdateRequest updateRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFound(id));
        Author author = authorRepository.findById(updateRequest.authorId())
                .orElseThrow(() -> new AuthorNotFound(updateRequest.authorId()));

        book.setAuthor(author);
        book.setGenre(updateRequest.genre());
        book.setTitle(updateRequest.title());
        book.setPublishedYear(updateRequest.publishedYear());
        return bookRepository.save(book);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }
}
