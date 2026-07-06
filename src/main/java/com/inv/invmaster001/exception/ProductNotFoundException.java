package com.inv.invmaster001.exception;

/**
 * Exception thrown when a company is not found for the given identifier.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}