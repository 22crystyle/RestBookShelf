package org.example.exception;

public class BookNotFound extends NotFoundException {
    public BookNotFound(Long id) {
        super("Book with id=" + id + " not found");
    }
}
