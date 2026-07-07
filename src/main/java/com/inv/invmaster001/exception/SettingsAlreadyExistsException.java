package com.inv.invmaster001.exception;

/**
 * Exception thrown when attempting to register a company that already exists.
 */
public class SettingsAlreadyExistsException extends RuntimeException {
    public SettingsAlreadyExistsException(String message) {
        super(message);
    }
}