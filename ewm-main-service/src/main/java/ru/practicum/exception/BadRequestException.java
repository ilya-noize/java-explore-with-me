package ru.practicum.exception;

/**
 * Send status 400
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String error) {
        super(error);
    }
}
