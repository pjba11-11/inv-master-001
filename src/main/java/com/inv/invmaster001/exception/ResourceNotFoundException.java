package com.inv.invmaster001.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * Mapped to HTTP 404 (Not Found) by the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
