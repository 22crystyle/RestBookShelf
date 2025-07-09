package org.example.exception;

public class AuthorNotFound extends EntityNotFoundException {
    public AuthorNotFound(Long id) {
        super(id, "Author with id=" + id + " not found");
    }
}
