package com.inv.invmaster001.exception;

/**
 * Exception thrown when attempting to register a company that already exists.
 */
public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}