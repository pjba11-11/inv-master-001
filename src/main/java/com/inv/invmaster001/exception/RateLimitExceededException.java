package com.inv.invmaster001.exception;

/**
 * Thrown when a caller retries an expensive operation (e.g. an LLM-backed
 * insights call) before a short per-key cooldown window has elapsed.
 */
public class RateLimitExceededException extends RuntimeException {

    private final long retryAfterSeconds;

    public RateLimitExceededException(String message, long retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
