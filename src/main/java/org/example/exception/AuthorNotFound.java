package org.example.exception;

public class AuthorNotFound extends NotFoundException {
    public AuthorNotFound(Long id) {
        super("Author with id=" + id + " not found");
    }
}
