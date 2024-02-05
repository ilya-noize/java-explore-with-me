package ru.practicum.exception;

/**
 * Send status 403
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
