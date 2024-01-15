package ru.practicum.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
    }

    public BadRequestException(String error) {
        super(error);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
