package org.example.exception;

import lombok.Getter;

@Getter
public abstract class EntityNotFoundException extends RuntimeException {
    private Long entityId;

    public EntityNotFoundException(Long entityId) {
        super("Entity with id=" + entityId + " not found");
    }

    public EntityNotFoundException(Long entityId, String message) {
        super(message);
        this.entityId = entityId;
    }

}
