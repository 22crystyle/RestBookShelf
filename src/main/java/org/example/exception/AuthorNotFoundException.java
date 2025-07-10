package org.example.exception;

public class AuthorNotFoundException extends EntityNotFoundException {
    public AuthorNotFoundException(Long id) {
        super(id, "Author with id=" + id + " not found");
    }
}
