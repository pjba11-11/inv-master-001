package com.inv.invmaster001.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles exceptions globally across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle Validation exceptions (method argument not valid).
     * Returns a map of field errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                     org.springframework.web.context.request.WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        // Combine messages into a single string or keep as map; we'll create a concatenated message for simplicity
        String message = "Validation failed: " + errors.toString();
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle RateLimitExceededException globally.
     * Returns 429 with a Retry-After header indicating when the caller
     * may retry the throttled operation.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorDetails> handleRateLimitExceededException(RateLimitExceededException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()))
                .body(errorDetails);
    }

    /**
     * Handle RuntimeException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle BadCredentialsException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(org.springframework.security.authentication.BadCredentialsException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Invalid email or password",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle DisabledException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.authentication.DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleDisabledException(org.springframework.security.authentication.DisabledException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "User account is disabled",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle LockedException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.authentication.LockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleLockedException(org.springframework.security.authentication.LockedException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "User account is locked",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle AuthenticationCredentialsNotFoundException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleAuthenticationCredentialsNotFoundException(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Authentication credentials not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle UsernameNotFoundException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(org.springframework.security.core.userdetails.UsernameNotFoundException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "User not found",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle AuthenticationException globally (catch-all for other auth exceptions).
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Authentication failed: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle AccessDeniedException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Access denied: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle Exception globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle IllegalArgumentException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle IllegalStateException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle NullPointerException globally.
     *
     * @param ex      the exception
     * @param request the current request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDetails> handleNullPointerException(NullPointerException ex, org.springframework.web.context.request.WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Null pointer exception occurred",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}