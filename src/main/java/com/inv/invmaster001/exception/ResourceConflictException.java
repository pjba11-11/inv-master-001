package com.inv.invmaster001.exception;

/**
 * Exception thrown when a request conflicts with the current state of a resource,
 * such as attempting to create something that already exists.
 * Mapped to HTTP 409 (Conflict) by the global exception handler.
 */
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}
