package org.example.exception;

public class BookNotFound extends EntityNotFoundException {
    public BookNotFound(Long id) {
        super(id, "Book with id=" + id + " not found");
    }
}
