package org.example.exception;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(Long id) {
        super(id, "Book with id=" + id + " not found");
    }
}
