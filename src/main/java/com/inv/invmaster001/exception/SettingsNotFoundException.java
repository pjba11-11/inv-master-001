package com.inv.invmaster001.exception;

/**
 * Exception thrown when a company is not found for the given identifier.
 */
public class SettingsNotFoundException extends RuntimeException {
    public SettingsNotFoundException(String message) {
        super(message);
    }
}