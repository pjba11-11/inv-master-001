package com.inv.invmaster001.exception;

/**
 * Exception thrown when a company is not found for the given identifier.
 */
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}