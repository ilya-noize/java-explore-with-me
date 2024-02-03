package ru.practicum.exception;

/**
 * send status 409
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String error) {
        super(error);
    }
}
